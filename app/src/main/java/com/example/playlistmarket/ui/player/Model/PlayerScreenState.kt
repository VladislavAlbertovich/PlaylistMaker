package com.example.playlistmarket.ui.player.Model

sealed interface PlayerScreenState {
    object Default : PlayerScreenState
    object Prepared : PlayerScreenState
    object Paused : PlayerScreenState
    object Playing : PlayerScreenState
}