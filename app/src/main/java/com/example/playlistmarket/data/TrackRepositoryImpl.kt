package com.example.playlistmarket.data

import android.content.SharedPreferences
import com.example.playlistmarket.domain.impl.TrackRepository
import com.example.playlistmarket.domain.models.Track
import com.google.gson.Gson

class TrackRepositoryImpl(private val sharedPreferences: SharedPreferences) : TrackRepository {

    override fun getTrack(): Track {
        val json = sharedPreferences.getString(OPEN_TRACK_KEY, null)
        return Gson().fromJson(json, Track::class.java)
    }
}