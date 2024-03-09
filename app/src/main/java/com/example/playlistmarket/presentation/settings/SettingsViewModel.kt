package com.example.playlistmarket.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmarket.domain.settings.interactors.SettingsInteractor

class SettingsViewModel(settingsInteractor: SettingsInteractor) : ViewModel() {

    private val liveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(
        settingsInteractor.getTheme()
    )
    fun observeLiveData(): LiveData<Boolean> = liveData

    fun updateLiveData(newValue: Boolean){
        liveData.postValue(newValue)
    }

}