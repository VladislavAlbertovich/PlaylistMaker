package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.player.callbacks.TracksConsumer

interface FindTracksUseCase {
    fun findTracks(expression: String, consumer: TracksConsumer)
}