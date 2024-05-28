package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(private val searchHistoryRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override suspend fun addTrackToSearchHistory(newTrack: Track) {
        searchHistoryRepository.addTrackToSearchHistory(newTrack)
    }

    override fun clearTracksFromSearchHistory() {
        searchHistoryRepository.clearTracksFromSearchHistory()
    }

    override suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>>{
        return searchHistoryRepository.getTracksFromSearchHistory()
    }
}