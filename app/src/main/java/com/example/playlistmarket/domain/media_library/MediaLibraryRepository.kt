package com.example.playlistmarket.domain.media_library

import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaLibraryRepository {
    fun favoriteTracks(): Flow<List<Track>>

    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromFavorites(track: Track)
    suspend fun favoriteTracksIds(): Flow<List<Int>>
}