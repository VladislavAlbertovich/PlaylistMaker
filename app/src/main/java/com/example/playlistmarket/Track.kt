package com.example.playlistmarket

import com.google.gson.annotations.SerializedName
import java.net.URI
import java.net.URL
import java.util.Date

data class Track(
    val previewUrl: String,
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