package com.example.playlistmarket.presentation.media_library

import com.example.playlistmarket.domain.media_library.models.Playlist

interface PlaylistsState {
    object Empty : PlaylistsState
    data class Content(val data: List<Playlist>) : PlaylistsState
}
