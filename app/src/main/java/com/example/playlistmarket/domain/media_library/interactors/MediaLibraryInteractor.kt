package com.example.playlistmarket.domain.media_library.interactors

import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryInteractor {
    suspend fun addTrackToFavorites(track: Track)
    suspend fun favoriteTracks(): Flow<List<Track>>
    suspend fun removeTrackFromFavorites(track: Track)
    suspend fun favoriteTracksIds(): Flow<List<Int>>
}