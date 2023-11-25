package com.example.playlistmarket

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Track(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String
)