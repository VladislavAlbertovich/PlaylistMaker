package com.example.playlistmarket.data.media_library

import com.example.playlistmarket.data.converters.MediaLibraryDatabaseConverter
import com.example.playlistmarket.data.db.MediaLibraryDatabase
import com.example.playlistmarket.domain.media_library.MediaLibraryRepository
import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaLibraryRepositoryImpl(
    private val database: MediaLibraryDatabase,
    private val converter: MediaLibraryDatabaseConverter
) : MediaLibraryRepository {
    override fun favoriteTracks(): Flow<List<Track>> = flow {
        val tracksEntities = database.trackDao().getFavoritesTracks().sortedByDescending { it.timeOfAddingOfToFavorites }
        val tracks = tracksEntities.map { trackEntity ->
            converter.map(trackEntity)
        }
        tracks.map {
            it.isFavorite = true
        }
        emit(tracks)
    }

    override suspend fun addTrackToFavorites(track: Track){
        val trackEntity = converter.map(track)
        database.trackDao().addToFavorites(trackEntity)
    }

    override suspend fun removeTrackFromFavorites(track: Track){
        val trackEntity = converter.map(track)
        database.trackDao().removeFromFavorites(trackEntity)
    }

    override suspend fun favoriteTracksIds(): Flow<List<Int>> = flow{
        val tracksIds = database.trackDao().getFavoritesTracksIds()
        emit(tracksIds)
    }


}