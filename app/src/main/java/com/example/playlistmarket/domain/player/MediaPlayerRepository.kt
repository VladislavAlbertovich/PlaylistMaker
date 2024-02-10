package com.example.playlistmarket.domain.player

import com.example.playlistmarket.data.player.impl.State
import com.example.playlistmarket.domain.search.models.Track

interface MediaPlayerRepository {

    fun getCurrentPosition(): Int
    fun startPlayer()
    fun playbackControl()
    fun pausePlayer()
    fun preparePlayer(track: Track?)
    fun playerDestroy()
    fun getPlayerState(): State
    fun onReset()
}