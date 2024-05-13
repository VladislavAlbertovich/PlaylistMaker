package com.example.playlistmarket.data.track.impl

import android.content.SharedPreferences
import com.example.playlistmarket.data.search.impl.OPEN_TRACK_KEY
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.HistoryTrackRepository
import com.google.gson.Gson

class HistoryTrackRepositoryImpl(private val sharedPreferences: SharedPreferences) : HistoryTrackRepository {

    override fun getTrack(): Track {
        val json = sharedPreferences.getString(OPEN_TRACK_KEY, null)
        return Gson().fromJson(json, Track::class.java)
    }

    override fun addTrackToSharedPreferences(track: Track){
        if (sharedPreferences.getString(OPEN_TRACK_KEY, null) != null) {
            sharedPreferences.apply {
                edit().remove(OPEN_TRACK_KEY).apply()
                edit().putString(OPEN_TRACK_KEY, Gson().toJson(track)).apply()
            }
        } else {
            sharedPreferences.edit().putString(OPEN_TRACK_KEY, Gson().toJson(track))?.apply()
        }
    }


}