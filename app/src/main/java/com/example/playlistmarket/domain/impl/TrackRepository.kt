package com.example.playlistmarket.domain.impl

import com.example.playlistmarket.domain.models.Track

interface TrackRepository {
    fun getTrack(): Track

}