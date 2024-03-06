package com.example.playlistmarket.domain.player.impl

import com.example.playlistmarket.data.player.impl.State
import com.example.playlistmarket.domain.player.MediaPlayerRepository
import com.example.playlistmarket.domain.player.MediaPlayerUseCase
import com.example.playlistmarket.domain.search.models.Track

class MediaPlayerUseCaseImpl(private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerUseCase {
    override fun preparePlayer(track: Track?) {
        mediaPlayerRepository.preparePlayer(track)
    }

    override fun playbackControl() {
        mediaPlayerRepository.playbackControl()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }
    override fun getPlayerState():State {
        return mediaPlayerRepository.getPlayerState()
    }

    override fun onReset() {
        mediaPlayerRepository.onReset()
    }
}
