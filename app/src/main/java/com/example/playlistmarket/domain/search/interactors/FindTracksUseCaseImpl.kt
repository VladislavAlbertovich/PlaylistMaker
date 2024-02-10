package com.example.playlistmarket.domain.search.interactors

import com.example.playlistmarket.domain.player.callbacks.TracksConsumer
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.utils.Resource
import java.util.concurrent.Executors

class FindTracksUseCaseImpl(private val searchRepository: SearchRepository) : FindTracksUseCase {

    private val executor = Executors.newCachedThreadPool()
    override fun findTracks(expression: String, consumer: TracksConsumer) {
        executor.execute {
            when (val tracks = searchRepository.findTracks(expression)) {
                is Resource.Error -> consumer.consume(null, tracks.message)
                is Resource.Success -> consumer.consume(tracks.data, null)
            }
        }
    }
}