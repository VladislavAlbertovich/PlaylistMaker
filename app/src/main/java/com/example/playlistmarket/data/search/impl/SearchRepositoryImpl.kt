package com.example.playlistmarket.data.search.impl

import com.example.playlistmarket.data.search.network.NetworkClient
import com.example.playlistmarket.data.search.dto.ITunesSearchRequest
import com.example.playlistmarket.data.search.dto.ITunesSearchResponse
import com.example.playlistmarket.data.search.dto.TrackDTO
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.utils.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun findTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(ITunesSearchRequest(expression))
        return when(response.result){
            -1 -> Resource.Error("Загрузка не удалась. Проверьте подключение к интернету")
            200 -> Resource.Success((response as ITunesSearchResponse).tracks.map{
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
            })
            else -> Resource.Error("Проблемы со связью")
        }
    }
}