package com.example.playlistmarket.domain.search

import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    suspend fun addTrackToSearchHistory(newTrack: Track)
    fun clearTracksFromSearchHistory()
    suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>>
}