package com.example.playlistmarket.data.mediaplayer

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmarket.domain.api.MediaPlayerRepository
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.api.MediaPlayerRepository.StartPlayerListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.TimeFragmentListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.PausePlayerListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.PlayerOnPreparedListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.PlayerOnCompletionListener

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())
    override fun startPlayer(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener
    ) {
        mediaPlayer.start()
        startPlayerListener.listen()
        playerState = STATE_PLAYING
        handler.postDelayed(createUpdateTimeFragmentTask(timeFragmentListener), DELAY)
    }

    override fun pausePlayer(pausePlayerListener: PausePlayerListener) {
        mediaPlayer.pause()
        pausePlayerListener.listen()
        playerState = STATE_PAUSED
    }

    override fun preparePlayer(
        track: Track,
        playerOnPreparedListener: PlayerOnPreparedListener,
        playerOnCompletionListener: PlayerOnCompletionListener
    ) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            playerOnPreparedListener.listen()
        }
        mediaPlayer.setOnCompletionListener {
            playerOnCompletionListener.listen()
            playerState = STATE_PREPARED
            mediaPlayer.reset()
            preparePlayer(track, playerOnPreparedListener, playerOnCompletionListener)
        }
    }

    override fun playbackControl(
        startPlayerListener: StartPlayerListener,
        timeFragmentListener: TimeFragmentListener,
        pauseClickListener: PausePlayerListener
    ) {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> startPlayer(startPlayerListener, timeFragmentListener)
            STATE_PLAYING -> pausePlayer(pauseClickListener)
        }
    }

    override fun createUpdateTimeFragmentTask(timeFragmentListener: TimeFragmentListener): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    timeFragmentListener.listen()
                    handler.postDelayed(this, DELAY)
                }

                if (playerState == STATE_PAUSED) {
                    handler.removeCallbacks(this)
                }

                if (playerState == STATE_PREPARED) {
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
        playerState = STATE_DEFAULT
        handlerRemoveCallbacks(timeFragmentListener)
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    companion object {
        private const val DELAY = 500L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}


