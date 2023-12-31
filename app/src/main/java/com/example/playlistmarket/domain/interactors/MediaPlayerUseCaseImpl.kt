package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.repository.MediaPlayerRepository
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.callbacks.PausePlayerListener
import com.example.playlistmarket.domain.callbacks.PlayerOnCompletionListener
import com.example.playlistmarket.domain.callbacks.PlayerOnPreparedListener
import com.example.playlistmarket.domain.callbacks.StartPlayerListener
import com.example.playlistmarket.domain.callbacks.TimeFragmentListener

class MediaPlayerUseCaseImpl(private val mediaPlayerRepository: MediaPlayerRepository): MediaPlayerUseCase {
    override fun preparePlayer(
        track: Track?,
        playerOnPreparedListener: PlayerOnPreparedListener,
        playerOnCompletionListener: PlayerOnCompletionListener
    ) {
        mediaPlayerRepository.preparePlayer(
            track,
            playerOnPreparedListener,
            playerOnCompletionListener
        )
    }

    override fun playbackControl(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener,
        pausePlayerListener: PausePlayerListener
    ) {
        mediaPlayerRepository.playbackControl(startPlayerListener, timeFragmentListener, pausePlayerListener)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun playerDestroy(timeFragmentListener: TimeFragmentListener) {
        mediaPlayerRepository.playerDestroy(timeFragmentListener)
    }

    override fun pausePlayer(pausePlayerListener: PausePlayerListener) {
        mediaPlayerRepository.pausePlayer(pausePlayerListener)
    }

    override fun handlerRemoveCallbacks(timeFragmentListener: TimeFragmentListener) {
        mediaPlayerRepository.handlerRemoveCallbacks(timeFragmentListener)
    }
}