package com.example.playlistmarket.data.search.impl

import com.example.playlistmarket.data.search.dto.ITunesSearchRequest
import com.example.playlistmarket.data.search.dto.ITunesSearchResponse
import com.example.playlistmarket.data.search.network.NO_INTERNET
import com.example.playlistmarket.data.search.network.NetworkClient
import com.example.playlistmarket.data.search.network.OK_REQUEST
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun findTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(ITunesSearchRequest(expression))
        when(response.result){
            NO_INTERNET -> emit(Resource.Error("Загрузка не удалась. Проверьте подключение к интернету"))
            OK_REQUEST -> emit(Resource.Success((response as ITunesSearchResponse).tracks.map{
                Track(
                    previewUrl = it.previewUrl,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTime,
                    artworkUrl100 = it.artworkUrl100,
                    trackId = it.trackId,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country
                )
            }))
            else -> emit(Resource.Error("Проблемы со связью"))
        }
    }
}