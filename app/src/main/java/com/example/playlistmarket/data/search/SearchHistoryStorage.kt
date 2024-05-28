package com.example.playlistmarket.data.search

import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryStorage {
    suspend fun addTrackToSearchHistory(newTrack: Track)
    fun clearTracksFromSearchHistory()
    suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>>
}