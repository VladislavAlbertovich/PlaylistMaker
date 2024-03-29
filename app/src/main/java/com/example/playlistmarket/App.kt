package com.example.playlistmarket

import android.app.Application
import android.app.UiModeManager
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.di.dataModule
import com.example.playlistmarket.di.interactorModule
import com.example.playlistmarket.di.repositoryModule
import com.example.playlistmarket.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val THEME_SHARED_PREFERENCE = "theme_shared_preference"
const val THEME_SWITCH_KEY = "key_for_switch_theme"

class App : Application() {

    private var isDarkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        sharedPrefs = getSharedPreferences(THEME_SHARED_PREFERENCE, MODE_PRIVATE)
        isDarkTheme = sharedPrefs.getBoolean(THEME_SWITCH_KEY, false)
        switchTheme(isDarkTheme)
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        isDarkTheme = isDarkThemeEnabled
        sharedPrefs.edit().putBoolean(THEME_SWITCH_KEY, isDarkThemeEnabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        ComponentActivity.UI_MODE_SERVICE
        val uiManager = this.getSystemService(ComponentActivity.UI_MODE_SERVICE) as UiModeManager
        if (isDarkThemeEnabled) {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES)
        } else {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO)
        }
    }
}