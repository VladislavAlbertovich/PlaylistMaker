package com.example.playlistmarket.presentation.player

import com.example.playlistmarket.domain.media_library.models.Playlist

sealed interface TrackAddingState {
    object Ready: TrackAddingState
    data class Done(val playlist: Playlist): TrackAddingState
    data class NotDone(val playlist: Playlist): TrackAddingState
}