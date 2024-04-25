package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FindTracksUseCase {
    fun findTracks(expression: String): Flow<Pair<List<Track>?, String?>>
}