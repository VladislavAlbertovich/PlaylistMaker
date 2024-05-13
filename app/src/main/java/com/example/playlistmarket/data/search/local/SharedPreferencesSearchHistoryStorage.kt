package com.example.playlistmarket.data.search.local

import android.content.SharedPreferences
import com.example.playlistmarket.data.db.MediaLibraryDatabase
import com.example.playlistmarket.data.search.SearchHistoryStorage
import com.example.playlistmarket.data.search.impl.TRACKS_FROM_HISTORY_KEY
import com.example.playlistmarket.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPreferencesSearchHistoryStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val database: MediaLibraryDatabase
) : SearchHistoryStorage {

    override suspend fun addTrackToSearchHistory(newTrack: Track) {
        val historySearchTracks = ArrayList<Track>()
        getTracksFromSearchHistory().collect{
            historySearchTracks.addAll(it)
        }
        historySearchTracks.removeAll { it.trackId == newTrack.trackId }
        historySearchTracks.add(0, newTrack)
        while (historySearchTracks.size > 10) {
            historySearchTracks.removeAt(historySearchTracks.size - 1)
        }
        addTracksToSharedPreferences(historySearchTracks)
    }

    override fun clearTracksFromSearchHistory() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }

    override suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>> = flow {
        val jsonTracks = sharedPreferences
            .getString(TRACKS_FROM_HISTORY_KEY, null)
        val tracks = ArrayList<Track>()
        if (jsonTracks != null) {
            tracks.addAll(extractTracksFromJson(jsonTracks))
        }
        val checkedIsFavoriteTracks = tracks.map { track ->
            if (isFavorite(track)) {
                Track(
                    track.previewUrl,
                    track.trackName,
                    track.artistName,
                    track.trackTimeMillis,
                    track.artworkUrl100,
                    track.trackId,
                    track.collectionName,
                    track.releaseDate,
                    track.primaryGenreName,
                    track.country,
                    true
                )
            } else {
                Track(
                    track.previewUrl,
                    track.trackName,
                    track.artistName,
                    track.trackTimeMillis,
                    track.artworkUrl100,
                    track.trackId,
                    track.collectionName,
                    track.releaseDate,
                    track.primaryGenreName,
                    track.country,
                    false
                )
            }
        }
        emit(ArrayList(checkedIsFavoriteTracks))
    }

    private fun extractTracksFromJson(json: String?): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun addTracksToSharedPreferences(tracks: ArrayList<Track>) {
        sharedPreferences
            .edit()
            .putString(TRACKS_FROM_HISTORY_KEY, createJsonFromTracks(tracks))
            ?.apply()
    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return gson.toJson(tracks)
    }

    private suspend fun isFavorite(track: Track): Boolean {
        val favoriteTracksIds = database.trackDao().getFavoritesTracksIds()
        var isFavorite = false
        favoriteTracksIds.forEach { favoriteTrackId ->
            if (track.trackId == favoriteTrackId) {
                isFavorite = true
            }
        }
        return isFavorite
    }
}
