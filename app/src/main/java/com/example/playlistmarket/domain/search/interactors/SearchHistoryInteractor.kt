package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    suspend fun addTrackToSearchHistory(newTrack: Track)
    fun clearTracksFromSearchHistory()
    suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>>
}