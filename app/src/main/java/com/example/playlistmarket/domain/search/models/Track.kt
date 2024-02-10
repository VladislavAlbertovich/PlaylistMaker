package com.example.playlistmarket.domain.search.models

import java.util.Date

data class Track(
    val previewUrl: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String
) {
    fun artworkUrl512() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

}