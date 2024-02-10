package com.example.playlistmarket.domain.track.impl

import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.domain.track.TrackRepository

class TrackUseCaseImpl(private val trackRepository: TrackRepository): TrackUseCase {
    override fun execute(): Track {
        return trackRepository.getTrack()
    }

    override fun addTrackToSharedPreferences(track: Track){
        trackRepository.addTrackToSharedPreferences(track)
    }
}