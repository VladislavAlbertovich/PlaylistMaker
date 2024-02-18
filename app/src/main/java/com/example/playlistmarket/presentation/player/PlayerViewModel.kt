package com.example.playlistmarket.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.App
import com.example.playlistmarket.Creator
import com.example.playlistmarket.data.player.impl.State
import com.example.playlistmarket.domain.player.MediaPlayerUseCase
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.ui.player.Model.PlayerScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val playerUseCase: MediaPlayerUseCase, trackUseCase: TrackUseCase) : ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val track = trackUseCase.execute()

    private val playerStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
    fun observePlayerScreenState(): LiveData<PlayerScreenState> = playerStateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData

    private val trackLiveData = MutableLiveData(track)
    fun observeTrack(): LiveData<Track> = trackLiveData

    companion object {
        private const val DELAY = 100L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val playerUseCase = Creator.provideMediaPlayerUseCase()
                val trackUseCase = Creator.provideTrackUseCase(this[APPLICATION_KEY] as App)
                PlayerViewModel(playerUseCase, trackUseCase)
            }
        }
    }

    fun startUpdateTime() {
        mainThreadHandler.post(updateTime())
    }

    fun stopUpdateTime() {
        mainThreadHandler.removeCallbacks(updateTime())
    }

    fun playbackControl() {
        playerUseCase.playbackControl()
        updateLiveData()
    }
    fun preparePlayer() {
        playerUseCase.preparePlayer(track)
    }

    fun onPause() {
        playerUseCase.pausePlayer()
        stopUpdateTime()
    }

    fun onReset() {
        playerUseCase.onReset()
        stopUpdateTime()
    }

    fun onDestroy(){
        stopUpdateTime()
    }

    private fun updateLiveData() {
        when (playerUseCase.getPlayerState()) {
            State.DEFAULT -> {
                playerStateLiveData.postValue(PlayerScreenState.Default)
            }

            State.PREPARED -> {
                playerStateLiveData.postValue(PlayerScreenState.Prepared)
            }

            State.PAUSED -> {
                playerStateLiveData.postValue(PlayerScreenState.Paused)
            }

            State.PLAYING -> {
                playerStateLiveData.postValue(PlayerScreenState.Playing)
            }
        }
    }

    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerUseCase != null){
                timeLiveData.postValue(
                    convertTime(
                        playerUseCase.getCurrentPosition()
                    )
                )
                mainThreadHandler.postDelayed(this, DELAY)
            }}
        }
    }
    private fun convertTime(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}