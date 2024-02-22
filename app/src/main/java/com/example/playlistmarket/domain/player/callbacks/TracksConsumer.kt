package com.example.playlistmarket.domain.player.callbacks

import com.example.playlistmarket.domain.search.models.Track

interface TracksConsumer {

    fun consume(tracks: List<Track>?, message: String?)
}