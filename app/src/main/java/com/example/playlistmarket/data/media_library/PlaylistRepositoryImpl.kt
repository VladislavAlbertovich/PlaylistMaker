package com.example.playlistmarket.data.media_library

import android.net.Uri
import com.example.playlistmarket.data.converters.MediaLibraryDatabaseConverter
import com.example.playlistmarket.data.db.MediaLibraryDatabase
import com.example.playlistmarket.data.db.entity.PlaylistEntity
import com.example.playlistmarket.domain.media_library.PlaylistRepository
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val mediaLibraryDatabase: MediaLibraryDatabase,
    private val gson: Gson,
    private val converter: MediaLibraryDatabaseConverter
) : PlaylistRepository {
    override suspend fun createPlaylist(name: String, description: String, cover: Uri?) {
        val playlistEntity = PlaylistEntity(
            title = name,
            description = description,
            cover = cover.toString(),
            tracksIds = gson.toJson(emptyList<Int>()),
            countOfTracks = 0
        )
        mediaLibraryDatabase.playlistDao().addItem(playlistEntity)
    }

    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> = flow {
        val playlistEntity = mediaLibraryDatabase.playlistDao().getItem(id)
        val playlist = converter.map(playlistEntity)
        emit(playlist)
    }

    override fun getPlaylistsFromLibrary(): Flow<List<Playlist>> = flow {
        val playlistEntityList = mediaLibraryDatabase.playlistDao().getAllItems()
        val itemList = convertFromPlaylistEntity(playlistEntityList.sortedByDescending { it.id })
        emit(itemList)
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        val idList = createIdListFromJson(playlist.tracksIds)
        idList.add(0, track.trackId)
        val idListString = gson.toJson(idList)
        val updatedPlaylist = playlist.copy(tracksIds = idListString, tracksCount = idList.size)
        val playlistEntity = converter.map(updatedPlaylist)
        mediaLibraryDatabase.playlistDao().updateItem(playlistEntity)
    }

    override suspend fun addTrackToSaved(track: Track) {
        val trackEntity = converter.mapToSavedTrackEntity(track)
        mediaLibraryDatabase.savedTrackDao().addItem(trackEntity)    }

    override fun checkTrackContains(track: Track, playlist: Playlist): Boolean {
        val ids = createIdListFromJson(playlist.tracksIds)
        val id = track.trackId
        return (ids.contains(id))
    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity -> converter.map(playlistEntity) }
    }

    private fun createIdListFromJson(json: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, listType)
    }
}