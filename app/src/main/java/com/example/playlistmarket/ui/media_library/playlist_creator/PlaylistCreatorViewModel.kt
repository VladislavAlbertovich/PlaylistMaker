package com.example.playlistmarket.ui.media_library.playlist_creator

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.media_library.interactors.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {

    fun createPlaylist(name: String, description: String, cover: Uri?) {
        viewModelScope.launch { playlistsInteractor.createPlaylist(name, description, cover) }
    }
}