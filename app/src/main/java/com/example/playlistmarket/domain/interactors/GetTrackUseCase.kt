package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.repository.TrackRepository
import com.example.playlistmarket.domain.models.Track

class GetTrackUseCase(private val getTrackRepository: TrackRepository) {
    fun execute(): Track {
        return getTrackRepository.getTrack()
    }
}