package com.example.playlistmarket.di

import com.example.playlistmarket.domain.media_library.interactors.MediaLibraryInteractor
import com.example.playlistmarket.domain.media_library.interactors.MediaLibraryInteractorImpl
import com.example.playlistmarket.domain.player.MediaPlayerUseCase
import com.example.playlistmarket.domain.player.impl.MediaPlayerUseCaseImpl
import com.example.playlistmarket.domain.resource_provider.ResourceProviderInteractor
import com.example.playlistmarket.domain.resource_provider.impl.ResourceProviderInteractorImpl
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCase
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCaseImpl
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractor
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractorImpl
import com.example.playlistmarket.domain.settings.interactors.SettingsInteractor
import com.example.playlistmarket.domain.settings.interactors.SettingsInteractorImpl
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.domain.track.impl.TrackUseCaseImpl
import org.koin.dsl.module

val interactorModule = module {
    //search
    single<FindTracksUseCase> { FindTracksUseCaseImpl(get()) }
    single<SearchHistoryInteractor> { SearchHistoryInteractorImpl(get()) }
    //track
    single<TrackUseCase> { TrackUseCaseImpl(get()) }
    //ResourceProvider
    single<ResourceProviderInteractor> { ResourceProviderInteractorImpl(get()) }
    //player
    single<MediaPlayerUseCase> { MediaPlayerUseCaseImpl(get()) }
    //settings
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
    //MediaLibraryInteractor
    single<MediaLibraryInteractor> { MediaLibraryInteractorImpl(get()) }
}