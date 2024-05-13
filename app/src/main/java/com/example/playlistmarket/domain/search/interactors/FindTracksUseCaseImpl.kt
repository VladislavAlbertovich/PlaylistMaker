package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FindTracksUseCaseImpl(private val searchRepository: SearchRepository) : FindTracksUseCase {

    override fun findTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return searchRepository.findTracks(expression).map { resourceListTrack ->
            when(resourceListTrack) {
                is Resource.Error -> Pair(null, resourceListTrack.message)
                is Resource.Success -> Pair(resourceListTrack.data, null)
            }
        }
    }
}