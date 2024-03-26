package com.example.playlistmarket.presentation.media_library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksViewModel(private val favoriteTracks: String): ViewModel() {

    private val favoriteTracksLiveData = MutableLiveData(favoriteTracks)
    fun observeFavoriteTracks(): LiveData<String> = favoriteTracksLiveData

}