package com.example.playlistmarket.presentation.media_library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel(private val playlists: String): ViewModel() {
    private val liveData = MutableLiveData(playlists)
    fun observeLiveData(): LiveData<String> = liveData
}