package com.example.playlistmarket.data.search.impl

import com.example.playlistmarket.data.search.SearchHistoryStorage
import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.models.Track

const val TRACKS_FROM_HISTORY_KEY = "TRACKS_FROM_HISTORY_KEY"
const val OPEN_TRACK_KEY = "OPEN_TRACK_KEY"
const val TRACKS_HISTORY_SHARED_PREFERENCES_KEY = "TRACKS_HISTORY_SHARED_PREFERENCES_KEY"

class SearchHistoryRepositoryImpl(private val searchHistoryStorage: SearchHistoryStorage): SearchHistoryRepository {

    override fun addTrackToSearchHistory(newTrack: Track) {
        searchHistoryStorage.addTrackToSearchHistory(newTrack)
    }

    override fun clearTracksFromSearchHistory() {
        searchHistoryStorage.clearTracksFromSearchHistory()
    }

    override fun getTracksFromSearchHistory(): ArrayList<Track> {
        return searchHistoryStorage.getTracksFromSearchHistory()
    }
}


