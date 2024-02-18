package com.example.playlistmarket.domain.resource_provider

interface ResourceProviderRepository {
    fun getString(stringRes: Int): String
}