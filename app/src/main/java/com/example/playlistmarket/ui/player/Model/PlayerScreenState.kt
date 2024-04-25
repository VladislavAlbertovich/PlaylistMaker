package com.example.playlistmarket.ui.player.Model

sealed class PlayerScreenState(val progress: String) {
    class Default : PlayerScreenState("00:00")
    class Prepared : PlayerScreenState("00:00")
    class Paused(currentProgress: String) : PlayerScreenState(currentProgress)
    class Playing(currentProgress: String) : PlayerScreenState(currentProgress)
}