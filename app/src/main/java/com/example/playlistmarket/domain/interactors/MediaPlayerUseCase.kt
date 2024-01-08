package com.example.playlistmarket.domain.interactors

import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.unsorted.PausePlayerListener
import com.example.playlistmarket.unsorted.PlayerOnCompletionListener
import com.example.playlistmarket.unsorted.PlayerOnPreparedListener
import com.example.playlistmarket.unsorted.StartPlayerListener
import com.example.playlistmarket.unsorted.TimeFragmentListener

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
