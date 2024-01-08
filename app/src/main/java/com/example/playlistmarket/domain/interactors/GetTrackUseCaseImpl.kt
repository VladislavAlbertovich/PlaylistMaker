package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.repository.TrackRepository
import com.example.playlistmarket.domain.models.Track

class GetTrackUseCaseImpl(private val trackRepository: TrackRepository): GetTrackUseCase {
    override fun execute(): Track {
        return trackRepository.getTrack()
    }
}