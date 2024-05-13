package com.example.playlistmarket.domain.track.impl

import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.HistoryTrackRepository
import com.example.playlistmarket.domain.track.TrackUseCase

class TrackUseCaseImpl(private val historyTrackRepository: HistoryTrackRepository): TrackUseCase {
    override fun execute(): Track {
        return historyTrackRepository.getTrack()
    }

    override fun addTrackToSharedPreferences(track: Track){
        historyTrackRepository.addTrackToSharedPreferences(track)
    }
}