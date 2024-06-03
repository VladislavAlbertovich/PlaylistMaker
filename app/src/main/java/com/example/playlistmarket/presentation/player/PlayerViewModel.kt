package com.example.playlistmarket.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.data.player.impl.State
import com.example.playlistmarket.domain.media_library.interactors.MediaLibraryInteractor
import com.example.playlistmarket.domain.media_library.interactors.PlaylistsInteractor
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.player.MediaPlayerUseCase
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.ui.player.Model.PlayerScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerUseCase: MediaPlayerUseCase,
    trackUseCase: TrackUseCase,
    private val mediaLibraryInteractor: MediaLibraryInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) :
    ViewModel() {

    private val track = trackUseCase.execute()

    init {
        preparePlayer(track)
    }

    private val playerStateLiveData =
        MutableLiveData<PlayerScreenState>(PlayerScreenState.Default())

    fun observePlayerScreenState(): LiveData<PlayerScreenState> = playerStateLiveData

    private val trackLiveData = MutableLiveData(track)
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val isFavoriteLiveData = MutableLiveData(track.isFavorite)
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    private val playlists = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = playlists

    private val trackAddingState = MutableLiveData<TrackAddingState>()
    fun observeTrackAddingStatus(): LiveData<TrackAddingState> = trackAddingState

    private var stateJob: Job? = null

    companion object {
        private const val DELAY = 300L
    }

    fun stopUpdatePlayerState() {
        stateJob?.cancel()
    }

    fun playbackControl() {
        updatePlayerStateLiveData()
        playerUseCase.playbackControl()
    }

    fun pausePlayer() {
        playerUseCase.pausePlayer()
        stopUpdatePlayerState()
    }

    fun resetPlayer() {
        playerUseCase.onReset()
        stopUpdatePlayerState()
    }

    suspend fun onFavoriteClicked() {
        if (track.isFavorite) {
            mediaLibraryInteractor.removeTrackFromFavorites(track)
            track.isFavorite = false
            isFavoriteLiveData.postValue(track.isFavorite)
        } else {
            mediaLibraryInteractor.addTrackToFavorites(track)
            track.isFavorite = true
            isFavoriteLiveData.postValue(track.isFavorite)
        }
    }

    private fun updatePlayerStateLiveData() {
        stateJob = viewModelScope.launch {
            while (true) {
                delay(DELAY)
                when (playerUseCase.getPlayerState()) {
                    State.DEFAULT -> {
                        playerStateLiveData.postValue(PlayerScreenState.Default())
                    }

                    State.PREPARED -> {
                        playerStateLiveData.postValue(PlayerScreenState.Prepared())
                    }

                    State.PAUSED -> {
                        playerStateLiveData.postValue(
                            PlayerScreenState.Paused(
                                convertTime(
                                    playerUseCase.getCurrentPosition()
                                )
                            )
                        )
                    }

                    State.PLAYING -> {
                        playerStateLiveData.postValue(
                            PlayerScreenState.Playing(
                                convertTime(playerUseCase.getCurrentPosition())
                            )
                        )
                    }
                }
            }
        }
    }

    private fun preparePlayer(track: Track) {
        playerUseCase.preparePlayer(track)
    }

    private fun convertTime(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    fun getPlaylistsFromLibrary() {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistsFromLibrary()
                .collect { itemList -> playlists.postValue(itemList)}
        }
    }

    private fun checkTrackContains(track: Track, playlist: Playlist): Boolean {
        return playlistsInteractor.checkTrackContains(track, playlist)
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (checkTrackContains(track, playlist)) {
            trackAddingState.postValue(TrackAddingState.NotDone(playlist))
        } else {
            addTrackToSaved(track)
            updatePlaylistAndReload(track, playlist)
            trackAddingState.postValue(TrackAddingState.Done(playlist))
        }
    }

    private fun addTrackToSaved(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.addTrackToSaved(track)
        }
    }

    private fun updatePlaylistAndReload(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            updatePlaylist(track, playlist)
            getPlaylistsFromLibrary()
        }
    }

    private fun updatePlaylist(track: Track, playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(track, playlist)
        }
    }
}