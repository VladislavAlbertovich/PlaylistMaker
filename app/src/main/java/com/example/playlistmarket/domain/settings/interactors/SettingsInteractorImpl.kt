package com.example.playlistmarket.domain.settings.interactors

import com.example.playlistmarket.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getTheme(): Boolean {
         return settingsRepository.getTheme()
    }
}