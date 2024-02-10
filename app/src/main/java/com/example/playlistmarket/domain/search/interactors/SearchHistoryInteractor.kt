package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.search.models.Track

interface SearchHistoryInteractor {

    fun addTrackToSearchHistory(newTrack: Track)
    fun clearTracksFromSearchHistory()
    fun getTracksFromSearchHistory(): ArrayList<Track>
}