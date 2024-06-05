package com.example.playlistmarket.domain.media_library.interactors

import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(name: String, description: String, cover: String?)
    suspend fun editPlaylist(id: Int, title: String, description: String, cover: String? )
    fun getPlaylistFromLibrary(id: Int): Flow<Playlist>
    fun getPlaylistsFromLibrary(): Flow<List<Playlist>>
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    suspend fun addTrackToSaved(track: Track)
    fun checkTrackContains(track: Track, playlist: Playlist): Boolean
    fun getTracksFromPlaylist(tracksIds: String): Flow<List<Track>>
    suspend fun sharePlaylist(playlistId: Int)
    suspend fun removePlaylist(playlistId: Int?)
    suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int)
    fun saveImageToPrivateStorage(uri: String, playlistTitle: String)
}
