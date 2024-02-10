package com.example.playlistmarket

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmarket.data.search.network.NetworkClient
import com.example.playlistmarket.data.search.network.RetrofitNetworkClient
import com.example.playlistmarket.data.track.impl.TrackRepositoryImpl
import com.example.playlistmarket.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchHistoryRepositoryImpl
import com.example.playlistmarket.data.search.impl.SearchRepositoryImpl
import com.example.playlistmarket.domain.track.TrackRepository
import com.example.playlistmarket.domain.player.MediaPlayerRepository
import com.example.playlistmarket.domain.track.TrackUseCase
import com.example.playlistmarket.domain.track.impl.TrackUseCaseImpl
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCase
import com.example.playlistmarket.domain.search.interactors.FindTracksUseCaseImpl
import com.example.playlistmarket.domain.player.MediaPlayerUseCase
import com.example.playlistmarket.domain.player.impl.MediaPlayerUseCaseImpl
import com.example.playlistmarket.domain.search.SearchHistoryRepository
import com.example.playlistmarket.domain.search.SearchRepository
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractor
import com.example.playlistmarket.domain.search.interactors.SearchHistoryInteractorImpl
import com.example.playlistmarket.presentation.search.SearchViewModel

object Creator {

    fun provideMediaPlayerUseCase(): MediaPlayerUseCase {
        return MediaPlayerUseCaseImpl(getMediaPlayerRepository())
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun provideTrackUseCase(sharedPreferences: SharedPreferences?): TrackUseCase {
        return TrackUseCaseImpl(getTrackRepository(sharedPreferences))
    }

    private fun getTrackRepository(sharedPreferences: SharedPreferences?): TrackRepository {
        return TrackRepositoryImpl(sharedPreferences)
    }

    fun provideFindTracksUseCase(context: Context): FindTracksUseCase {
        return FindTracksUseCaseImpl(getTracksRepository(RetrofitNetworkClient(context)))
    }

    private fun getTracksRepository(networkClient: NetworkClient): SearchRepository {
        return SearchRepositoryImpl(networkClient)
    }

    private fun getSearchHistoryRepository(sharedPreferences: SharedPreferences): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideSearchHistoryInteractor(sharedPreferences: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(sharedPreferences))
    }
}