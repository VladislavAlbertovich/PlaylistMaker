package com.example.playlistmarket.presentation.media_library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.media_library.interactors.MediaLibraryInteractor
import com.example.playlistmarket.domain.resource_provider.ResourceProviderInteractor
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.domain.track.TrackUseCase
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val mediaLibraryInteractor: MediaLibraryInteractor, private val resourceProviderInteractor: ResourceProviderInteractor, private val trackUseCase: TrackUseCase): ViewModel() {

    private val favoriteTracksStateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeFavoriteTracksState(): LiveData<FavoriteTracksState> = favoriteTracksStateLiveData

    private fun processResult(tracks:List<Track>) {
        if (tracks.isEmpty()){
            favoriteTracksStateLiveData.postValue(FavoriteTracksState.Empty(resourceProviderInteractor.getString(
                R.string.media_library_empty)))
        } else {
            favoriteTracksStateLiveData.postValue(FavoriteTracksState.Content(tracks))
        }
    }

    fun provideTrack(track: Track) {
        trackUseCase.addTrackToSharedPreferences(track)
    }

    fun fillData(){
        viewModelScope.launch {
            mediaLibraryInteractor.favoriteTracks().collect{
                processResult(it)
            }
        }
    }
}