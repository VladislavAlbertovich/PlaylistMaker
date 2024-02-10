package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.models.Track

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun addTrackToSearchHistory(newTrack: Track) {
        searchHistoryRepository.addTrackToSearchHistory(newTrack)
    }

    override fun clearTracksFromSearchHistory() {
        searchHistoryRepository.clearTracksFromSearchHistory()
    }

    override fun getTracksFromSearchHistory(): ArrayList<Track> {
        return searchHistoryRepository.getTracksFromSearchHistory()
    }
}