package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.callbacks.PausePlayerListener
import com.example.playlistmarket.domain.callbacks.PlayerOnCompletionListener
import com.example.playlistmarket.domain.callbacks.PlayerOnPreparedListener
import com.example.playlistmarket.domain.callbacks.StartPlayerListener
import com.example.playlistmarket.domain.callbacks.TimeFragmentListener

interface MediaPlayerRepository {

    fun getCurrentPosition(): Int
    fun startPlayer(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener
    )

    fun pausePlayer(pausePlayerListener: PausePlayerListener)
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

    fun createUpdateTimeFragmentTask(timeFragmentListener: TimeFragmentListener): Runnable
    fun handlerRemoveCallbacks(timeFragmentListener: TimeFragmentListener)
    fun playerDestroy(timeFragmentListener: TimeFragmentListener)

}