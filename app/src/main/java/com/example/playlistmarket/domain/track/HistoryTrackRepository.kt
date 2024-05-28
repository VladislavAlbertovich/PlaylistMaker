package com.example.playlistmarket.domain.track

import com.example.playlistmarket.domain.search.models.Track

interface HistoryTrackRepository {
    fun getTrack(): Track

    fun addTrackToSharedPreferences(track: Track)

}