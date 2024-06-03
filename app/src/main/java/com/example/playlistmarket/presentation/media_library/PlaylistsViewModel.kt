package com.example.playlistmarket.presentation.media_library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.domain.media_library.interactors.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistsInteractor): ViewModel() {

    private val state = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = state

    private fun renderState(state: PlaylistsState) { this.state.postValue(state) }

    fun updateState() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistsFromLibrary()
                .collect { data ->
                    if (data.isEmpty()) renderState(PlaylistsState.Empty)
                    else renderState(PlaylistsState.Content(data))
                }
        }
    }
}