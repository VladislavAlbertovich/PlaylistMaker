package com.example.playlistmarket

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmarket.data.repositories.TrackRepositoryImpl
import com.example.playlistmarket.data.repositories.MediaPlayerRepositoryImpl
import com.example.playlistmarket.domain.repository.TrackRepository
import com.example.playlistmarket.domain.repository.MediaPlayerRepository
import com.example.playlistmarket.domain.interactors.GetTrackUseCase
import com.example.playlistmarket.domain.interactors.GetTrackUseCaseImpl
import com.example.playlistmarket.domain.interactors.MediaPlayerUseCase
import com.example.playlistmarket.domain.interactors.MediaPlayerUseCaseImpl

object Creator {

    fun provideMediaPlayerUseCase(): MediaPlayerUseCase {
        return MediaPlayerUseCaseImpl(getMediaPlayerRepository())
    }

    private fun getMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(MediaPlayer())
    }

    fun getTrackUseCase(sharedPreferences: SharedPreferences): GetTrackUseCase {
        return GetTrackUseCaseImpl(provideGetTrackRepository(sharedPreferences))
    }

    private fun provideGetTrackRepository(sharedPreferences: SharedPreferences): TrackRepository {
        return TrackRepositoryImpl(sharedPreferences)
    }
}