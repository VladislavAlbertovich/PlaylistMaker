package com.example.playlistmarket

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACKS_KEY = "TRACKS_KEY"
const val TRACKS_HISTORY_SHARED_PREFERENCES_KEY = "TRACKS_HISTORY_SHARED_PREFERENCES_KEY"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun addTrackToSearchHistory(newTrack: Track) {
        val historySearchTracks: ArrayList<Track> = getTracksFromSharedPreferences()
        historySearchTracks.removeAll { it.trackId == newTrack.trackId }
        historySearchTracks.add(0, newTrack)
        while (historySearchTracks.size > 10){
            historySearchTracks.removeAt(historySearchTracks.size-1)
        }
        addTracksToSharedPreferences(historySearchTracks)
    }

    fun clearSharedPreferences(){
        sharedPreferences.edit().clear().apply()
    }

    fun getTracksFromSharedPreferences(): ArrayList<Track> {
        val jsonTracks = sharedPreferences.getString(TRACKS_KEY, null) ?: return ArrayList()
        return createTracksFromJson(jsonTracks)
    }

    private fun addTracksToSharedPreferences(tracks: ArrayList<Track>) {
        sharedPreferences.edit().putString(TRACKS_KEY, createJsonFromTracks(tracks)).apply()
    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    fun createTracksFromJson(json: String): ArrayList<Track> {
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }
}


