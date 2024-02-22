package com.example.playlistmarket

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmarket.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmarket.data.resource_provider.ResourceProviderRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchRepositoryImpl
import com.example.playlistmarket.data.search.network.NetworkClient
import com.example.playlistmarket.data.search.network.RetrofitNetworkClient
import com.example.playlistmarket.data.settings.SettingsRepositoryImpl
import com.example.playlistmarket.data.track.impl.TrackRepositoryImpl
import com.example.playlistmarket.domain.player.MediaPlayerRepository
import com.example.playlistmarket.domain.player.MediaPlayerUseCase
import com.example.playlistmarket.domain.player.impl.MediaPlayerUseCaseImpl
import com.example.playlistmarket.domain.resource_provider.ResourceProviderInteractor
import com.example.playlistmarket.domain.resource_provider.ResourceProviderRepository
import com.example.playlistmarket.domain.resource_provider.impl.ResourceProviderInteractorImpl
import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCase
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCaseImpl
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractor
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractorImpl
import com.example.playlistmarket.domain.settings.SettingsRepository
import com.example.playlistmarket.domain.settings.interactors.SettingsInteractor
import com.example.playlistmarket.domain.settings.interactors.SettingsInteractorImpl
import com.example.playlistmarket.domain.track.TrackRepository
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.domain.track.impl.TrackUseCaseImpl

object Creator {

    fun provideMediaPlayerUseCase(): MediaPlayerUseCase {
        return MediaPlayerUseCaseImpl(getMediaPlayerRepository())
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideTrackUseCase(context: Context): TrackUseCase {
        return TrackUseCaseImpl(getTrackRepository(context))
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(context)
    }

    fun provideFindTracksUseCase(context: Context): FindTracksUseCase {
        return FindTracksUseCaseImpl(getTracksRepository(RetrofitNetworkClient(context)))
    }

    private fun getTracksRepository(networkClient: NetworkClient): SearchRepository {
        return SearchRepositoryImpl(networkClient)
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun provideResourceProviderInteractor(context: Context): ResourceProviderInteractor {
        return ResourceProviderInteractorImpl(getResourceProviderRepository(context))
    }

    private fun getResourceProviderRepository(context: Context): ResourceProviderRepository {
        return ResourceProviderRepositoryImpl(context)
    }
}