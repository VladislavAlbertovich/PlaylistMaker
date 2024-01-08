package com.example.playlistmarket.domain.repository

import com.example.playlistmarket.domain.models.Track

interface TrackRepository {
    fun getTrack(): Track

}