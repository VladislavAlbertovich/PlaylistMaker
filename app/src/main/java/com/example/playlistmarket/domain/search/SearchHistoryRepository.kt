package com.example.playlistmarket.domain.search

import com.example.playlistmarket.domain.search.models.Track

interface SearchHistoryRepository {
    fun addTrackToSearchHistory(newTrack: Track)
    fun clearTracksFromSearchHistory()
    fun getTracksFromSearchHistory(): ArrayList<Track>
}