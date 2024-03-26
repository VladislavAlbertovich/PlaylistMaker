package com.example.playlistmarket.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.example.playlistmarket.THEME_SHARED_PREFERENCE
import com.example.playlistmarket.data.search.SearchHistoryStorage
import com.example.playlistmarket.data.search.impl.TRACKS_HISTORY_SHARED_PREFERENCES_KEY
import com.example.playlistmarket.data.search.local.SharedPreferencesSearchHistoryStorage
import com.example.playlistmarket.data.search.network.ITunesSearchApi
import com.example.playlistmarket.data.search.network.NetworkClient
import com.example.playlistmarket.data.search.network.RetrofitNetworkClient
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// модуль для получения SharedPreferences, Api сервиса, Gson'а, RetrofitNetworkClient, MediaPlayer

val dataModule = module{

    //Search

    //получаем ITunesSearchApi, который является зависимостью в RetrofitNetworkClient
    single<ITunesSearchApi> {
        Retrofit
            .Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApi::class.java)
    }

    //получаем RetrofitNetworkClient, который является зависимостью в SearchRepositoryImpl
    single<NetworkClient> { RetrofitNetworkClient(androidContext(), get()) }

    single { androidContext().getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE) }

    factory { Gson() }

    //получаем SearchHistoryStorage, который является зависимостью в SearchHistoryRepository
    single <SearchHistoryStorage>{ SharedPreferencesSearchHistoryStorage(get(), get())}

    //player
    factory { MediaPlayer() }
    //settings
    single(named("themeSharedPreference")) { androidContext().getSharedPreferences(
        THEME_SHARED_PREFERENCE, MODE_PRIVATE) }
}