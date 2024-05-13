package com.example.playlistmarket.ui.search.models

import com.example.playlistmarket.domain.search.models.Track

data class SearchState(val tracks: List<Track>, val isLoading: Boolean, val errorMessage: String, val additionalMessage: String)