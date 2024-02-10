package com.example.playlistmarket.presentation.settings

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.THEME_SHARED_PREFERENCE
import com.example.playlistmarket.THEME_SWITCH_KEY

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences(THEME_SHARED_PREFERENCE, MODE_PRIVATE)

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener { _, _ -> updateLiveData() }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }


    private val liveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(
        sharedPreferences.getBoolean(
            THEME_SWITCH_KEY, false
        )
    )
    fun observeLiveData(): LiveData<Boolean> = liveData

    private fun updateLiveData() {
        val isChecked = sharedPreferences.getBoolean(THEME_SWITCH_KEY, false)
        liveData.postValue(isChecked)
    }
}