package com.example.playlistmarket.data.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmarket.THEME_SHARED_PREFERENCE
import com.example.playlistmarket.THEME_SWITCH_KEY
import com.example.playlistmarket.domain.settings.SettingsRepository

class SettingsRepositoryImpl(context: Context): SettingsRepository {

    private val sharedPreferences = context.getSharedPreferences(THEME_SHARED_PREFERENCE, MODE_PRIVATE)
    override fun getTheme(): Boolean{
        return sharedPreferences.getBoolean(THEME_SWITCH_KEY, false)
    }
}