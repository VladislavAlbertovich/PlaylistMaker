package com.example.playlistmarket.data.search.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACKS_FROM_HISTORY_KEY = "TRACKS_FROM_HISTORY_KEY"
const val OPEN_TRACK_KEY = "OPEN_TRACK_KEY"
const val TRACKS_HISTORY_SHARED_PREFERENCES_KEY = "TRACKS_HISTORY_SHARED_PREFERENCES_KEY"

class SearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {

    private val sharedPreferences = context.getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)
    override fun addTrackToSearchHistory(newTrack: Track) {
        val historySearchTracks: ArrayList<Track> = getTracksFromSearchHistory()
        historySearchTracks.removeAll { it.trackId == newTrack.trackId }
        historySearchTracks.add(0, newTrack)
        while (historySearchTracks.size > 10) {
            historySearchTracks.removeAt(historySearchTracks.size - 1)
        }
        addTracksToSharedPreferences(historySearchTracks)
    }

    override fun clearTracksFromSearchHistory() {
        sharedPreferences.edit().clear().apply()
    }

    override fun getTracksFromSearchHistory(): ArrayList<Track> {
        val jsonTracks = sharedPreferences.getString(TRACKS_FROM_HISTORY_KEY, null) ?: return ArrayList()
        return extractTracksFromJson(jsonTracks)
    }

    private fun extractTracksFromJson(json: String): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun addTracksToSharedPreferences(tracks: ArrayList<Track>) {
        sharedPreferences.
        edit().
        putString(TRACKS_FROM_HISTORY_KEY, createJsonFromTracks(tracks))?.
        apply()
    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }
}


