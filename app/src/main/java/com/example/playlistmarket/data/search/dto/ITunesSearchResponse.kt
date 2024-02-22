package com.example.playlistmarket.data.search.dto

import com.example.playlistmarket.domain.search.models.Track
import com.google.gson.annotations.SerializedName

data class ITunesSearchResponse(
    val resultCount: Int, @SerializedName("results") val tracks: ArrayList<TrackDTO>
) : Response()