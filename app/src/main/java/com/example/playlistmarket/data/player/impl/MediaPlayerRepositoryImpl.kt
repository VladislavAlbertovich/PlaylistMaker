package com.example.playlistmarket.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmarket.domain.player.MediaPlayerRepository
import com.example.playlistmarket.domain.search.models.Track

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState = State.DEFAULT
    override fun startPlayer() {
        mediaPlayer.start()
        playerState = State.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = State.PAUSED
    }

    override fun preparePlayer(
        track: Track?
    ) {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = State.PREPARED

        }
        mediaPlayer.setOnCompletionListener {
            playerState = State.PREPARED
            mediaPlayer.seekTo(0)
        }
    }

    override fun playbackControl() {
        when (playerState) {
            State.PREPARED, State.PAUSED -> startPlayer()
            State.PLAYING -> pausePlayer()
            else -> {}
        }
    }

    override fun onReset() {
        mediaPlayer.reset()
        playerState = State.DEFAULT
    }

    override fun getCurrentPosition(): Int {
           return mediaPlayer.currentPosition

    }

    override fun getPlayerState() = playerState

}

enum class State {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}
