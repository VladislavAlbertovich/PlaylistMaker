package com.example.playlistmarket.domain.api

import com.example.playlistmarket.domain.models.Track

interface MediaPlayerRepository {

    fun getCurrentPosition(): Int
    fun startPlayer(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener
    )

    fun pausePlayer(pausePlayerListener: PausePlayerListener)
    fun preparePlayer(
        track: Track,
        playerOnPreparedListener: PlayerOnPreparedListener,
        playerOnCompletionListener: PlayerOnCompletionListener
    )

    fun playbackControl(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener,
        pausePlayerListener: PausePlayerListener
    )

    fun createUpdateTimeFragmentTask(timeFragmentListener: TimeFragmentListener): Runnable
    fun handlerRemoveCallbacks(timeFragmentListener: TimeFragmentListener)
    fun playerDestroy(timeFragmentListener: TimeFragmentListener)

    fun interface Listener {
        fun listen()
    }

    fun interface StartPlayerListener : Listener
    fun interface PausePlayerListener : Listener
    fun interface PlayerOnPreparedListener : Listener
    fun interface PlayerOnCompletionListener : Listener
    fun interface TimeFragmentListener : Listener
}