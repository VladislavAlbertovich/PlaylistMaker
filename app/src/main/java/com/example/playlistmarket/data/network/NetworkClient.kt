package com.example.playlistmarket.data.network

import com.example.playlistmarket.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}