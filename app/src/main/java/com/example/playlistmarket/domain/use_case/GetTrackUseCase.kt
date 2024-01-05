package com.example.playlistmarket.domain.use_case

import com.example.playlistmarket.domain.impl.TrackRepository
import com.example.playlistmarket.domain.models.Track

class GetTrackUseCase(private val getTrackRepository: TrackRepository) {
    fun execute(): Track {
        return getTrackRepository.getTrack()
    }
}