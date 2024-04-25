package com.example.playlistmarket.presentation.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.player.callbacks.TracksConsumer
import com.example.playlistmarket.domain.resource_provider.ResourceProviderInteractor
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCase
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractor
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.ui.search.models.SearchState

class SearchViewModel(
    private val findTracksUseCase: FindTracksUseCase,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val trackUseCase: TrackUseCase,
    private val resourceProviderInteractor: ResourceProviderInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    private val tracksHistoryLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    fun observeTracksHistory(): LiveData<ArrayList<Track>> = tracksHistoryLiveData

    init {
        tracksHistoryLiveData.postValue(getTracksFromHistory())
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }

    private val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        search(newSearchText)
    }

    private var tracks = ArrayList<Track>()

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDebounce(textRequest: String) {
        if (lastSearchText == textRequest){
            return
        }
        lastSearchText = textRequest
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun provideTrack(track: Track) {
        trackUseCase.addTrackToSharedPreferences(track)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearTracksFromSearchHistory()
        tracksHistoryLiveData.postValue(getTracksFromHistory())
    }

    fun updateHistoryAdapterTracks(track: Track) {
        searchHistoryInteractor.addTrackToSearchHistory(track)
        tracksHistoryLiveData.postValue(getTracksFromHistory())
    }

    private fun search(newSearchText: String) {

        if (newSearchText.isNotEmpty()) {
            lastSearchText = newSearchText

            renderState(SearchState(ArrayList(), true, "", ""))
        }

        findTracksUseCase.findTracks(newSearchText, object : TracksConsumer {
            override fun consume(tracks: List<Track>?, message: String?) {
                if (!tracks.isNullOrEmpty()) {
                    this@SearchViewModel.tracks.apply {
                        clear()
                        addAll(tracks)
                    }
                    renderState(
                        SearchState(
                            this@SearchViewModel.tracks,
                            false,
                            "",
                            ""
                        )
                    )


                } else if (message != null) {
                    renderState(
                        SearchState(
                            ArrayList(),
                            false,
                            resourceProviderInteractor.getString(R.string.download_fail),
                            message
                        )
                    )
                } else {
                    renderState(
                        SearchState(
                            ArrayList(),
                            false,
                            resourceProviderInteractor.getString(R.string.nothing_found),
                            ""
                        )
                    )
                }
            }
        })
    }

    private fun getTracksFromHistory() =
        searchHistoryInteractor.getTracksFromSearchHistory()

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }


    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }
}