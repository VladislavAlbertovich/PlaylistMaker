package com.example.playlistmarket.domain.track

import com.example.playlistmarket.domain.search.models.Track

interface TrackUseCase {
    fun execute(): Track
    fun addTrackToSharedPreferences(track: Track)
}