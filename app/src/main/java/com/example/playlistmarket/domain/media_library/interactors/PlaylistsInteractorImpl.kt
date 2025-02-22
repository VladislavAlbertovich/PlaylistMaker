package com.example.playlistmarket.domain.media_library.interactors

import com.example.playlistmarket.domain.media_library.PlaylistRepository
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistRepository) : PlaylistsInteractor {
    override suspend fun createPlaylist(name: String, description: String, cover: String?) {
        repository.createPlaylist(name, description, cover)
    }

    override suspend fun editPlaylist(id: Int, title: String, description: String, cover: String?) {
        repository.editPlaylist(id, title, description, cover)
    }

    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> {
        return repository.getPlaylistFromLibrary(id)
    }

    override fun getPlaylistsFromLibrary(): Flow<List<Playlist>> {
        return repository.getPlaylistsFromLibrary()
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        repository.updatePlaylist(track, playlist)
    }

    override suspend fun addTrackToSaved(track: Track) {
        repository.addTrackToSaved(track)
    }

    override fun checkTrackContains(track: Track, playlist: Playlist): Boolean {
        return repository.checkTrackContains(track, playlist)    }

    override fun getTracksFromPlaylist(tracksIds: String): Flow<List<Track>> {
        return repository.getTracksFromPlaylist(tracksIds)
    }

    override suspend fun sharePlaylist(playlistId: Int) {
        repository.sharePlaylist(playlistId)
    }

    override suspend fun removePlaylist(playlistId: Int?) {
        repository.removePlaylist(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int) {
        repository.removeTrackFromPlaylist(track, playlistId)
    }

    override fun saveImageToPrivateStorage(uri: String, playlistTitle: String){
        repository.saveImageToPrivateStorage(uri, playlistTitle)
    }
}