package com.example.playlistmarket.data.search

import com.example.playlistmarket.domain.search.models.Track

interface SearchHistoryStorage {
    fun addTrackToSearchHistory(newTrack: Track)
    fun clearTracksFromSearchHistory()
    fun getTracksFromSearchHistory(): ArrayList<Track>
}