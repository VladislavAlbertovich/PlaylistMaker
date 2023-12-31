package com.example.playlistmarket.data.network

import com.example.playlistmarket.data.dto.ITunesSearchRequest
import com.example.playlistmarket.data.dto.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchApi::class.java)
    override fun doRequest(dto: Any): Response {
        return if (dto is ITunesSearchRequest) {
            val resp = iTunesSearchService.getTracks(dto.expression).execute()
            val body = resp.body() ?: Response()
            body.apply { result = resp.code() }
        } else {
            Response().apply { result = 400 }
        }
    }
}