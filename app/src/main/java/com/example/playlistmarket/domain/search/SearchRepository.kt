package com.example.playlistmarket.domain.search

import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun findTracks(expression: String): Flow<Resource<List<Track>>>

}