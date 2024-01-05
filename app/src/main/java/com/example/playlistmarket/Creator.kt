package com.example.playlistmarket

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmarket.data.TrackRepositoryImpl
import com.example.playlistmarket.data.mediaplayer.MediaPlayerRepositoryImpl
import com.example.playlistmarket.domain.impl.TrackRepository
import com.example.playlistmarket.domain.api.MediaPlayerRepository
import com.example.playlistmarket.domain.use_case.GetMediaPlayerUseCase
import com.example.playlistmarket.domain.use_case.GetTrackUseCase

object Creator {
    fun getMediaPlayerUseCase(): GetMediaPlayerUseCase {
        return GetMediaPlayerUseCase(provideMediaPlayerRepository())
    }

    private fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun getTrackUseCase(sharedPreferences: SharedPreferences): GetTrackUseCase {
        return GetTrackUseCase(provideGetTrackRepository(sharedPreferences))
    }

    private fun provideGetTrackRepository(sharedPreferences: SharedPreferences): TrackRepository {
        return TrackRepositoryImpl(sharedPreferences)
    }
}