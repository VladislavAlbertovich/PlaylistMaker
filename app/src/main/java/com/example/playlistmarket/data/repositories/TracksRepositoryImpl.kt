package com.example.playlistmarket.data.repositories

import com.example.playlistmarket.data.network.NetworkClient
import com.example.playlistmarket.data.dto.ITunesSearchRequest
import com.example.playlistmarket.data.dto.ITunesSearchResponse
import com.example.playlistmarket.domain.repository.TracksRepository
import com.example.playlistmarket.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun getTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(ITunesSearchRequest(expression))
        if (response.result == 200) {
            return (response as ITunesSearchResponse).tracks.map {
                val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTime)
                Track(
                    it.previewUrl,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country
                )
            }
        } else {
            return emptyList()
        }
    }
}