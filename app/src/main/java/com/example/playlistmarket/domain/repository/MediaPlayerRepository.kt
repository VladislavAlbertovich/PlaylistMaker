package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.unsorted.PausePlayerListener
import com.example.playlistmarket.unsorted.PlayerOnCompletionListener
import com.example.playlistmarket.unsorted.PlayerOnPreparedListener
import com.example.playlistmarket.unsorted.StartPlayerListener
import com.example.playlistmarket.unsorted.TimeFragmentListener

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