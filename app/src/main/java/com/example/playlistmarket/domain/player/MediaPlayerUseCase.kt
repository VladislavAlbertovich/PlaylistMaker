package com.example.playlistmarket.domain.player

import com.example.playlistmarket.data.player.impl.State
import com.example.playlistmarket.domain.search.models.Track

interface MediaPlayerUseCase {

    fun preparePlayer(track: Track?)
    fun playbackControl()
    fun getCurrentPosition(): Int
    fun pausePlayer()
    fun startPlayer()
    fun getPlayerState():State
    fun onReset()

}
