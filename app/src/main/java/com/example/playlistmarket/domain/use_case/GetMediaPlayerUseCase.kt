package com.example.playlistmarket.domain.use_case

import com.example.playlistmarket.domain.api.MediaPlayerRepository

class GetMediaPlayerUseCase(private val mediaPlayerRepository: MediaPlayerRepository) {
    fun execute(): MediaPlayerRepository {
        return mediaPlayerRepository
    }
}