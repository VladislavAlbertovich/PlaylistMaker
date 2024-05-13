package com.example.playlistmarket.domain.media_library.interactors

import com.example.playlistmarket.domain.media_library.MediaLibraryRepository
import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class MediaLibraryInteractorImpl(private val mediaLibraryRepository: MediaLibraryRepository): MediaLibraryInteractor {

    override suspend fun favoriteTracks(): Flow<List<Track>> {
        return mediaLibraryRepository.favoriteTracks()
    }
    override suspend fun addTrackToFavorites(track: Track){
        mediaLibraryRepository.addTrackToFavorites(track)
    }
    override suspend fun removeTrackFromFavorites(track: Track){
        mediaLibraryRepository.removeTrackFromFavorites(track)
    }

    override suspend fun favoriteTracksIds(): Flow<List<Int>>{
        return mediaLibraryRepository.favoriteTracksIds()
    }
}