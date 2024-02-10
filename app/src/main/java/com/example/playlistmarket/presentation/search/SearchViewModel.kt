package com.example.playlistmarket.presentation.search

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.Creator
import com.example.playlistmarket.R
import com.example.playlistmarket.data.search.impl.TRACKS_FROM_HISTORY_KEY
import com.example.playlistmarket.data.search.impl.TRACKS_HISTORY_SHARED_PREFERENCES_KEY
import com.example.playlistmarket.domain.player.callbacks.TracksConsumer
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.ui.search.models.SearchState

class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val application = getApplication<Application>()

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)

    private val stateLiveData = MutableLiveData<SearchState>()
    private val tracksHistoryLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeState(): LiveData<SearchState> = stateLiveData
    fun observeTracksHistory(): LiveData<ArrayList<Track>> = tracksHistoryLiveData
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        search(newSearchText)
    }

    private val findTracksUseCase = Creator.provideFindTracksUseCase(application)
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(sharedPreferences)
    private val trackUseCase = Creator.provideTrackUseCase(sharedPreferences)

    private var tracks = ArrayList<Track>()

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == TRACKS_FROM_HISTORY_KEY) {
            val historyTracks = searchHistoryInteractor.getTracksFromSearchHistory()
            if (historyTracks.isEmpty()) {
                tracksHistoryLiveData.postValue(ArrayList())
            } else {
                tracksHistoryLiveData.postValue(historyTracks)
            }
        }
    }

    fun onCreate() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        tracksHistoryLiveData.postValue(getTracksFromHistory())
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun searchDebounce(textRequest: String) {
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

    fun historyNotEmpty() = getTracksFromHistory().size > 0
    fun updateHistoryAdapterTracks(track: Track) {
        searchHistoryInteractor.addTrackToSearchHistory(track)
        tracksHistoryLiveData.postValue(getTracksFromHistory())
    }

    private fun search(newSearchText: String) {

        lastSearchText = newSearchText

        renderState(SearchState(ArrayList(), true, "", ""))

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
                            application.getString(R.string.download_fail),
                            message
                        )
                    )
                } else {
                    renderState(
                        SearchState(
                            ArrayList(),
                            false,
                            application.getString(R.string.nothing_found),
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