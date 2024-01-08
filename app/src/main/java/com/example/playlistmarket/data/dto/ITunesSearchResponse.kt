package com.example.playlistmarket.data.dto

import com.example.playlistmarket.domain.models.Track
import com.google.gson.annotations.SerializedName

data class ITunesSearchResponse(
    val resultCount: Int, @SerializedName("results") val tracks: ArrayList<Track>
) : Response()