package com.example.playlistmarket.presentation.media_library

import com.example.playlistmarket.domain.search.models.Track

sealed interface FavoriteTracksState {
    data class Content(val tracks: List<Track>): FavoriteTracksState
    data class Empty (val message: String): FavoriteTracksState
}