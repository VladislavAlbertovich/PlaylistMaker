package com.example.playlistmarket.di

import com.example.playlistmarket.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmarket.data.resource_provider.ResourceProviderRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchRepositoryImpl
import com.example.playlistmarket.data.settings.SettingsRepositoryImpl
import com.example.playlistmarket.data.track.impl.TrackRepositoryImpl
import com.example.playlistmarket.domain.player.MediaPlayerRepository
import com.example.playlistmarket.domain.resource_provider.ResourceProviderRepository
import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.settings.SettingsRepository
import com.example.playlistmarket.domain.track.TrackRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    //Search
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    //Settings
    single<SettingsRepository> { SettingsRepositoryImpl(get(named("themeSharedPreference"))) }
    //Track
    single<TrackRepository> { TrackRepositoryImpl(get()) }
    //ResourceProvider
    single<ResourceProviderRepository>{ResourceProviderRepositoryImpl(androidContext())}
    //Player
    single<MediaPlayerRepository> { MediaPlayerRepositoryImpl(get()) }
}