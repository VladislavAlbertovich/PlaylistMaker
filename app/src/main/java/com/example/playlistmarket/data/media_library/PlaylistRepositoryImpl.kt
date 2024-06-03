package com.example.playlistmarket.data.media_library

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmarket.R
import com.example.playlistmarket.data.converters.MediaLibraryDatabaseConverter
import com.example.playlistmarket.data.db.MediaLibraryDatabase
import com.example.playlistmarket.data.db.entity.PlaylistEntity
import com.example.playlistmarket.data.db.entity.SavedTrackEntity
import com.example.playlistmarket.domain.media_library.PlaylistRepository
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.resource_provider.ResourceProviderRepository
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.utils.formatTextByNumbers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistRepositoryImpl(
    private val mediaLibraryDatabase: MediaLibraryDatabase,
    private val gson: Gson,
    private val converter: MediaLibraryDatabaseConverter,
    private val resourceProvider: ResourceProviderRepository,
    private val context: Context
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
        mediaLibraryDatabase.savedTrackDao().addItem(trackEntity)
    }

    override fun checkTrackContains(track: Track, playlist: Playlist): Boolean {
        val ids = createIdListFromJson(playlist.tracksIds)
        val id = track.trackId
        return (ids.contains(id))
    }

    override fun getTracksFromPlaylist(tracksIds: String): Flow<List<Track>> = flow {
        val savedTracksEntity = mediaLibraryDatabase.savedTrackDao().getAllItems()
        val savedTracks =
            convertFromSavedTrackEntity(savedTracksEntity.sortedByDescending { it.timeOfAddingOfToFavorites })
        val tracksIdsInt = createIdListFromJson(tracksIds)
        val filteredTracks = savedTracks.filter { track -> tracksIdsInt.contains(track.trackId) }
        val favoriteIdList = mediaLibraryDatabase.trackDao().getFavoritesTracksIds()
        for (track in filteredTracks) track.isFavorite = favoriteIdList.contains(track.trackId)
        emit(filteredTracks)
    }


    override suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int) {
        val playlistEntity = mediaLibraryDatabase.playlistDao().getItem(playlistId)
        val playlist = converter.map(playlistEntity)
        val tracksIdsInt = createIdListFromJson(playlist.tracksIds)
        val newTracksIdsInt = tracksIdsInt.filterNot { id -> id == track.trackId }
        val newTracksIdString = gson.toJson(newTracksIdsInt)
        val newPlaylist =
            playlist.copy(tracksIds = newTracksIdString, tracksCount = newTracksIdsInt.size)
        val newPlaylistEntity = converter.map(newPlaylist)
        mediaLibraryDatabase.playlistDao().updateItem(newPlaylistEntity)


        var isExistAnywhere = false
        val playlistLibrary = mediaLibraryDatabase.playlistDao().getAllItems()
        playlistLibrary.forEach { playlist ->
            val tracksIds = createIdListFromJson(playlist.tracksIds)
            if (tracksIds.contains(track.trackId)) {
                isExistAnywhere = true
                return@forEach
            }
        }
        if (!isExistAnywhere) {
            val trackEntity = converter.mapToSavedTrackEntity(track)
            mediaLibraryDatabase.savedTrackDao().removeItem(trackEntity)
        }
    }

    override suspend fun sharePlaylist(playlistId: Int) {
        var sharedPlaylistText = ""
        val playlistEntity = mediaLibraryDatabase.playlistDao().getItem(playlistId)
        val trackList = getTracksFromPlaylist(playlistEntity.tracksIds)
        trackList.collect { sharedPlaylistText = createSharedText(playlistEntity, it) }
        val title = resourceProvider.getString(R.string.share_playlist)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, sharedPlaylistText)
        val chooser = Intent.createChooser(intent, title)
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(context, chooser, null)
    }

    override suspend fun editPlaylist(id: Int, title: String, description: String, cover: Uri?) {
        val playlistEntity = mediaLibraryDatabase.playlistDao().getItem(id)
        val playlist = converter.map(playlistEntity)
        val updatedPlaylist = playlist.copy(
            title = title,
            description = description,
            cover = cover.toString()
        )
        mediaLibraryDatabase.playlistDao().updateItem(converter.map(updatedPlaylist))
    }

    override suspend fun removePlaylist(playlistId: Int?) {
        mediaLibraryDatabase.playlistDao().deletePlaylist(playlistId!!)
    }

    private fun createSharedText(playlistEntity: PlaylistEntity, trackList: List<Track>): String {
        var index = 1
        var text = "${playlistEntity.title}\n" +
                "${playlistEntity.description}\n" +
                formatTextByNumbers(trackList.size)

        trackList.forEach {
            text += "\n${index}.${it.artistName} - ${it.trackName} ${
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(it.trackTimeMillis)
            }"
            index++
        }
        return text
    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity -> converter.map(playlistEntity) }
    }

    private fun convertFromSavedTrackEntity(trackEntityList: List<SavedTrackEntity>): List<Track> {
        return trackEntityList.map { trackEntity -> converter.map(trackEntity) }
    }

    private fun createIdListFromJson(json: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, listType)
    }
}