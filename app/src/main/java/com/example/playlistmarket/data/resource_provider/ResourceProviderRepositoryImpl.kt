package com.example.playlistmarket.data.resource_provider

import android.content.Context
import com.example.playlistmarket.domain.resource_provider.ResourceProviderRepository


class ResourceProviderRepositoryImpl(private val context: Context) : ResourceProviderRepository {
    override fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }
}