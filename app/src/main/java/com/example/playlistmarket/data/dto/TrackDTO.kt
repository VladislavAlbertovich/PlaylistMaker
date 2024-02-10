package com.example.playlistmarket.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TrackDTO(
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