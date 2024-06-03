package com.example.playlistmarket.ui.media_library.playlist_creator

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.media_library.interactors.PlaylistsInteractor
import com.example.playlistmarket.domain.media_library.models.Playlist
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {

    private val currentPlaylist = MutableLiveData<Playlist>()
    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylist
    fun createPlaylist(name: String, description: String, cover: Uri?) {
        viewModelScope.launch { playlistsInteractor.createPlaylist(name, description, cover) }
    }

    fun getPlaylistFromLibrary(playlistId: Int?) {
        playlistId?.let {
            viewModelScope.launch {
                playlistsInteractor.getPlaylistFromLibrary(playlistId).collect {
                    currentPlaylist.postValue(it)
                }
            }
        }
    }
    fun updatePlaylist(id: Int, name: String, description: String, cover: Uri?) {
        viewModelScope.launch {
            playlistsInteractor.editPlaylist(id, name, description, cover)
        }
    }


}

