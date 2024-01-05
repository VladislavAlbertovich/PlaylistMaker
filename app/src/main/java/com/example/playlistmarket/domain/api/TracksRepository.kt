package com.example.playlistmarket.domain.api

import com.example.playlistmarket.domain.models.Track

interface TracksRepository {
    fun getTracks(expression: String): List<Track>

}