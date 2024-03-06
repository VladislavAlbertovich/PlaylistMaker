package com.example.playlistmarket.data.settings

import android.content.SharedPreferences
import com.example.playlistmarket.THEME_SWITCH_KEY
import com.example.playlistmarket.domain.settings.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences): SettingsRepository {
    override fun getTheme(): Boolean{
        return sharedPreferences.getBoolean(THEME_SWITCH_KEY, false)
    }
}