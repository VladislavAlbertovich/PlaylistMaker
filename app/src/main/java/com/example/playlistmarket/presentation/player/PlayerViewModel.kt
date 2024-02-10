package com.example.playlistmarket.presentation.player

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.Creator
import com.example.playlistmarket.data.player.impl.State
import com.example.playlistmarket.data.search.impl.TRACKS_HISTORY_SHARED_PREFERENCES_KEY
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.ui.player.Model.PlayerScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val playerUseCase = Creator.provideMediaPlayerUseCase()

    private val sharedPreferences =
        application.getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)
    private val track = Creator.provideTrackUseCase(sharedPreferences).execute()

    private val playerStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
    fun observePlayerScreenState(): LiveData<PlayerScreenState> = playerStateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData

    companion object {
        private const val DELAY = 100L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
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
    fun getTrack() = track



    fun preparePlayer(track: Track?) {
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
        playerUseCase.playerDestroy()
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
                timeLiveData.postValue(
                    convertTime(
                        playerUseCase.getCurrentPosition()
                    )
                )
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
    }
    private fun convertTime(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}