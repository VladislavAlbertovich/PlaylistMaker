package com.example.playlistmarket.domain.media_library.interactors

import android.net.Uri
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    suspend fun createPlaylist(name: String, description: String, cover: Uri?)
    fun getPlaylistFromLibrary(id: Int): Flow<Playlist>
    fun getPlaylistsFromLibrary(): Flow<List<Playlist>>
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    suspend fun addTrackToSaved(track: Track)
    fun checkTrackContains(track: Track, playlist: Playlist): Boolean

}
