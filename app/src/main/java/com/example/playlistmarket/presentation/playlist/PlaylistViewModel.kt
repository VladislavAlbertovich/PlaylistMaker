package com.example.playlistmarket.presentation.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.media_library.interactors.PlaylistsInteractor
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.utils.formatTextByMinutes
import com.example.playlistmarket.utils.formatTextByNumbers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val playlistInteractor: PlaylistsInteractor,
    private val trackUseCase: TrackUseCase
) : ViewModel() {


    private val currentPlaylist = MutableLiveData<Playlist>()
    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylist

    private val currentPlaylistTracks = MutableLiveData<List<Track>>()
    fun observeCurrentPlaylistTracks(): LiveData<List<Track>> = currentPlaylistTracks

    private val playlistInfo = MutableLiveData<Pair<String, String>>()
    fun observePlaylistInfo(): LiveData<Pair<String, String>> = playlistInfo

    private val contentExist: MutableLiveData<Boolean> = MutableLiveData()
    fun observeContentExist(): LiveData<Boolean> = contentExist

    private val emptyPlaylistMessage = MutableLiveData<Boolean>()
    fun observeEmptyShareMessage(): LiveData<Boolean> = emptyPlaylistMessage

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistFromLibrary(playlistId)
                .collect { playlist ->
                    currentPlaylist.postValue(playlist)
                    getPlaylistTracks(playlist.tracksIds)
                }
        }
    }

    private fun getPlaylistTracks(tracksIds: String) {
        viewModelScope.launch {
            playlistInteractor
                .getTracksFromPlaylist(tracksIds)
                .collect { trackList ->
                    currentPlaylistTracks.postValue(trackList)
                    getPlaylistInfo(trackList)
                    contentExist.postValue(trackList.isNotEmpty())
                }
        }
    }

    private fun getPlaylistInfo(trackList: List<Track>) {
        var playlistDuration = 0L
        trackList.forEach { track -> playlistDuration += track.trackTimeMillis }
        val playlistDurationAfterFormat =
            SimpleDateFormat("mm", Locale.getDefault()).format(playlistDuration).toInt()
        val pair = Pair(
            formatTextByMinutes(playlistDurationAfterFormat),
            formatTextByNumbers(trackList.size)
        )
        playlistInfo.postValue(pair)
    }


    fun provideTrack(track: Track) {
        trackUseCase.addTrackToSharedPreferences(track)
    }

    fun sharePlaylist(playlistId: Int) {
        viewModelScope.launch {
            if (contentExist.value == true) {
                playlistInteractor.sharePlaylist(playlistId)
            } else {
                emptyPlaylistMessage.postValue(true)
                delay(300)
                emptyPlaylistMessage.postValue(false)
            }
        }
    }
    fun removePlaylistFromLibrary(playlistId: Int?) {
        viewModelScope.launch{
            playlistInteractor.removePlaylist(playlistId)
        }
    }
    fun removeTrackFromPlaylist(track: Track, playlistId: Int){
        viewModelScope.launch {
            playlistInteractor.removeTrackFromPlaylist(track, playlistId)
            getPlaylist(playlistId)
        }
    }
}
