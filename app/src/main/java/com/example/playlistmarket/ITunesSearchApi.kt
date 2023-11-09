package com.example.playlistmarket

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun getTracks(@Query ("term") text: String): Call<ITunesSearchResponse>
}

data class ITunesSearchResponse(
    val resultCount: Int, @SerializedName("results") val tracks: ArrayList<Track>
)