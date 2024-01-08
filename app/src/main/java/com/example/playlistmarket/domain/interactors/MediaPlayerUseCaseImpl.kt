package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.repository.MediaPlayerRepository
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.unsorted.PausePlayerListener
import com.example.playlistmarket.unsorted.PlayerOnCompletionListener
import com.example.playlistmarket.unsorted.PlayerOnPreparedListener
import com.example.playlistmarket.unsorted.StartPlayerListener
import com.example.playlistmarket.unsorted.TimeFragmentListener

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