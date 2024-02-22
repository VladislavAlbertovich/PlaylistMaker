package com.example.playlistmarket.domain.resource_provider.impl

import com.example.playlistmarket.domain.resource_provider.ResourceProviderInteractor
import com.example.playlistmarket.domain.resource_provider.ResourceProviderRepository

class ResourceProviderInteractorImpl(private val resourceProviderRepository: ResourceProviderRepository) : ResourceProviderInteractor {

    override fun getString(stringRes: Int): String{
        return resourceProviderRepository.getString(stringRes)
    }
}