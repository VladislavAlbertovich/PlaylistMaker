package com.example.playlistmarket.domain.media_library.models

data class Playlist(
    val id: Int,
    val title: String,
    val description: String,
    val cover: String?,
    val tracksIds: String,
    val tracksCount: Int
)
