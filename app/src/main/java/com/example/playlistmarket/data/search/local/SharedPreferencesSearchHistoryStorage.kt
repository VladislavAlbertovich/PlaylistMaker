package com.example.playlistmarket.data.search.local

import android.content.SharedPreferences
import com.example.playlistmarket.data.search.SearchHistoryStorage
import com.example.playlistmarket.data.search.impl.TRACKS_FROM_HISTORY_KEY
import com.example.playlistmarket.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesSearchHistoryStorage(private val sharedPreferences: SharedPreferences, private val gson: Gson): SearchHistoryStorage {

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
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }

    override fun getTracksFromSearchHistory(): ArrayList<Track> {
        val jsonTracks = sharedPreferences
            .getString(TRACKS_FROM_HISTORY_KEY, null)?: return ArrayList()
        return extractTracksFromJson(jsonTracks)
    }

    private fun extractTracksFromJson(json: String): ArrayList<Track> {
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
}