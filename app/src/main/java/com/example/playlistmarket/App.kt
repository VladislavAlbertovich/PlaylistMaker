package com.example.playlistmarket

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val THEME_SHARED_PREFERENCE = "theme_shared_preference"
const val THEME_SWITCH_KEY = "key_for_switch_theme"
class App: Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(THEME_SHARED_PREFERENCE, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_SWITCH_KEY, false)
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        sharedPrefs.edit().putBoolean(THEME_SWITCH_KEY, darkThemeEnabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}