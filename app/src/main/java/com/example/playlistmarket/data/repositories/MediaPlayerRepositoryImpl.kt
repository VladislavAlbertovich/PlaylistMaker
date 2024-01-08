package com.example.playlistmarket.data.repositories

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmarket.domain.repository.MediaPlayerRepository
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.unsorted.PausePlayerListener
import com.example.playlistmarket.unsorted.PlayerOnCompletionListener
import com.example.playlistmarket.unsorted.PlayerOnPreparedListener
import com.example.playlistmarket.unsorted.StartPlayerListener
import com.example.playlistmarket.unsorted.TimeFragmentListener

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState = State.DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    override fun startPlayer(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener
    ) {
        mediaPlayer.start()
        startPlayerListener.listen()
        playerState = State.PLAYING
        handler.postDelayed(createUpdateTimeFragmentTask(timeFragmentListener), DELAY_MILLIS)
    }

    override fun pausePlayer(pausePlayerListener: PausePlayerListener) {
        mediaPlayer.pause()
        pausePlayerListener.listen()
        playerState = State.PAUSED
    }

    override fun preparePlayer(
        track: Track?,
        playerOnPreparedListener: PlayerOnPreparedListener,
        playerOnCompletionListener: PlayerOnCompletionListener
    ) {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = State.PREPARED
            playerOnPreparedListener.listen()
        }
        mediaPlayer.setOnCompletionListener {
            playerOnCompletionListener.listen()
            playerState = State.PREPARED
            mediaPlayer.reset()
            preparePlayer(track, playerOnPreparedListener, playerOnCompletionListener)
        }
    }

    override fun playbackControl(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener,
        pausePlayerListener: PausePlayerListener
    ) {
        when (playerState) {
            State.PREPARED, State.PAUSED -> startPlayer(startPlayerListener, timeFragmentListener)
            State.PLAYING-> pausePlayer(pausePlayerListener)
            else -> {}
        }
    }

    override fun createUpdateTimeFragmentTask(timeFragmentListener: TimeFragmentListener): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == State.PLAYING) {
                    timeFragmentListener.listen()
                    handler.postDelayed(this, DELAY_MILLIS)
                }

                if (playerState == State.PAUSED) {
                    handler.removeCallbacks(this)
                }

                if (playerState == State.PREPARED) {
                    timeFragmentListener.listen()
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    override fun handlerRemoveCallbacks(timeFragmentListener: TimeFragmentListener) {
        handler.removeCallbacks(createUpdateTimeFragmentTask(timeFragmentListener))
    }

    override fun playerDestroy(timeFragmentListener: TimeFragmentListener) {
        mediaPlayer.release()
        playerState = State.DEFAULT
        handlerRemoveCallbacks(timeFragmentListener)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    companion object {
        private const val DELAY_MILLIS = 500L
    }
}

enum class State {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}


