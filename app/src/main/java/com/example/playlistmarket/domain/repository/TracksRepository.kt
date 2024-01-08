package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Track

interface TracksRepository {
    fun getTracks(expression: String): List<Track>

}