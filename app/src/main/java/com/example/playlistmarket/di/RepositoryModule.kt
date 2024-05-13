package com.example.playlistmarket.di

import com.example.playlistmarket.data.media_library.MediaLibraryRepositoryImpl
import com.example.playlistmarket.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmarket.data.resource_provider.ResourceProviderRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchRepositoryImpl
import com.example.playlistmarket.data.settings.SettingsRepositoryImpl
import com.example.playlistmarket.data.track.impl.HistoryTrackRepositoryImpl
import com.example.playlistmarket.domain.media_library.MediaLibraryRepository
import com.example.playlistmarket.domain.player.MediaPlayerRepository
import com.example.playlistmarket.domain.resource_provider.ResourceProviderRepository
import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.settings.SettingsRepository
import com.example.playlistmarket.domain.track.HistoryTrackRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    //Search
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    //Settings
    single<SettingsRepository> { SettingsRepositoryImpl(get(named("themeSharedPreference"))) }
    //Track
    single<HistoryTrackRepository> { HistoryTrackRepositoryImpl(get()) }
    //ResourceProvider
    single<ResourceProviderRepository> { ResourceProviderRepositoryImpl(androidContext()) }
    //Player
    single<MediaPlayerRepository> { MediaPlayerRepositoryImpl(get()) }
    single<MediaLibraryRepository> { MediaLibraryRepositoryImpl(get(), get()) }
}