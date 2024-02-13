package com.example.playlistmarket.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.App
import com.example.playlistmarket.Creator
import com.example.playlistmarket.domain.settings.interactors.SettingsInteractor

class SettingsViewModel(settingsInteractor: SettingsInteractor) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val settingsInteractor =
                    Creator.provideSettingsInteractor(this[APPLICATION_KEY] as App)
                SettingsViewModel(settingsInteractor)
            }
        }
    }

    private val liveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(
        settingsInteractor.getTheme()
    )
    fun observeLiveData(): LiveData<Boolean> = liveData
}