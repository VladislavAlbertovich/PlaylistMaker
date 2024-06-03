package com.example.playlistmarket.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.resource_provider.ResourceProviderInteractor
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCase
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractor
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.ui.search.models.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val findTracksUseCase: FindTracksUseCase,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackUseCase: TrackUseCase,
    private val resourceProviderInteractor: ResourceProviderInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    private val tracksHistoryLiveData = MutableLiveData<ArrayList<Track>>()
    private var searchJob: Job? = null
    fun observeState(): LiveData<SearchState> = stateLiveData
    fun observeTracksHistory(): LiveData<ArrayList<Track>> = tracksHistoryLiveData

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }

    private var lastSearchText: String = ""

    fun searchDebounce(textRequest: String) {
        if (lastSearchText == textRequest) {
            return
        }

        lastSearchText = textRequest

        searchJob?.cancel()

        viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            val newSearchText = lastSearchText
            if (newSearchText.isNotEmpty()) {
                search(newSearchText)
            }
        }
    }

    fun provideTrack(track: Track) {
        trackUseCase.addTrackToSharedPreferences(track)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearTracksFromSearchHistory()
    }

    suspend fun updateHistoryAdapterTracks(track: Track) {
        searchHistoryInteractor.addTrackToSearchHistory(track)
    }

    fun search(newSearchText: String) {

        if (newSearchText.isNotEmpty()) {
            lastSearchText = newSearchText
            renderState(SearchState(ArrayList(), true, "", ""))
        }
        searchJob = viewModelScope.launch {
            findTracksUseCase.findTracks(newSearchText)
                .collect {
                    processResult(it.first, it.second)
                }
        }
    }

    fun updateState() {
        viewModelScope.launch {
            if (lastSearchText.isNotEmpty()) {
                findTracksUseCase.findTracks(lastSearchText).collect {
                    processResult(it.first, it.second)
                }
            }
            searchHistoryInteractor.getTracksFromSearchHistory().collect {
                tracksHistoryLiveData.postValue(it)
            }
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(tracks: List<Track>?, message: String?) {
        when {
            !tracks.isNullOrEmpty() -> {
                renderState(SearchState(tracks, false, "", ""))
            }

            message != null -> {
                renderState(
                    SearchState(
                        ArrayList(),
                        false,
                        resourceProviderInteractor.getString(R.string.download_fail),
                        message
                    )
                )
            }

            else -> renderState(
                SearchState(
                    ArrayList(),
                    false,
                    resourceProviderInteractor.getString(R.string.nothing_found),
                    ""
                )
            )
        }
    }
}