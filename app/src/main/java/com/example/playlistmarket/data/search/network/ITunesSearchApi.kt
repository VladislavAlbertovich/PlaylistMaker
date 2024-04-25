package com.example.playlistmarket.data.search.network

import com.example.playlistmarket.data.search.dto.ITunesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") text: String): ITunesSearchResponse
}
