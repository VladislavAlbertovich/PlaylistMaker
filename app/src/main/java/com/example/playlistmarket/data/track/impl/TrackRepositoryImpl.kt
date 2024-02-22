package com.example.playlistmarket.data.track.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmarket.data.search.impl.OPEN_TRACK_KEY
import com.example.playlistmarket.data.search.impl.TRACKS_HISTORY_SHARED_PREFERENCES_KEY
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackRepository
import com.google.gson.Gson

class TrackRepositoryImpl(context: Context) : TrackRepository {

    private val sharedPreferences = context.getSharedPreferences(
        TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)

    override fun getTrack(): Track {
        val json = sharedPreferences?.getString(OPEN_TRACK_KEY, null)
        return Gson().fromJson(json, Track::class.java)
    }

    override fun addTrackToSharedPreferences(track: Track){
        if (sharedPreferences?.getString(OPEN_TRACK_KEY, null) != null) {
            sharedPreferences.apply {
                edit().remove(OPEN_TRACK_KEY).apply()
                edit().putString(OPEN_TRACK_KEY, Gson().toJson(track)).apply()
            }
        } else {
            sharedPreferences?.edit()?.putString(OPEN_TRACK_KEY, Gson().toJson(track))?.apply()
        }
    }
}