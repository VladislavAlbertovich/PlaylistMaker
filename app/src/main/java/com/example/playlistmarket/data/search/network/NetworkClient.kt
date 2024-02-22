package com.example.playlistmarket.data.search.network

import com.example.playlistmarket.data.search.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}