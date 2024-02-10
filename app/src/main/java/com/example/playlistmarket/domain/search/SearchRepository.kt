package com.example.playlistmarket.domain.search

import com.example.playlistmarket.data.search.dto.TrackDTO
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.utils.Resource

interface SearchRepository {

    fun findTracks(expression: String): Resource<List<Track>>

}