package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.callbacks.PausePlayerListener
import com.example.playlistmarket.domain.callbacks.PlayerOnCompletionListener
import com.example.playlistmarket.domain.callbacks.PlayerOnPreparedListener
import com.example.playlistmarket.domain.callbacks.StartPlayerListener
import com.example.playlistmarket.domain.callbacks.TimeFragmentListener

interface MediaPlayerUseCase {

    fun preparePlayer(
        track: Track?,
        playerOnPreparedListener: PlayerOnPreparedListener,
        playerOnCompletionListener: PlayerOnCompletionListener
    )

    fun playbackControl(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener,
        pausePlayerListener: PausePlayerListener
    )

    fun getCurrentPosition(): Int
    fun playerDestroy(timeFragmentListener: TimeFragmentListener)
    fun pausePlayer(pausePlayerListener: PausePlayerListener)
    fun handlerRemoveCallbacks(timeFragmentListener: TimeFragmentListener)
}
