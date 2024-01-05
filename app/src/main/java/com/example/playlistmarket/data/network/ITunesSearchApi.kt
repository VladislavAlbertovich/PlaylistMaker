package com.example.playlistmarket.data.network

import com.example.playlistmarket.data.dto.ITunesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun getTracks(@Query("term") text: String): Call<ITunesSearchResponse>
}
