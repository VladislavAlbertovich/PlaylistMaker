package com.example.playlistmarket.data.search.network

import com.example.playlistmarket.data.search.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}