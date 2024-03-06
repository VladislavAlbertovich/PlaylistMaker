package com.example.playlistmarket.di

import com.example.playlistmarket.presentation.player.PlayerViewModel
import com.example.playlistmarket.presentation.search.SearchViewModel
import com.example.playlistmarket.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{
        SearchViewModel(get(), get(), get(), get())
    }
    viewModel{
        PlayerViewModel(get(), get())
    }
    viewModel{
        SettingsViewModel(get())
    }
}