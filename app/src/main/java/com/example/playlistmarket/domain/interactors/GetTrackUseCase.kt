package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.models.Track

interface GetTrackUseCase {
    fun execute(): Track
}