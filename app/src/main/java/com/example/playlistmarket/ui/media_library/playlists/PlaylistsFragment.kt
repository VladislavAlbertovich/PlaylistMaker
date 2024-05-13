package com.example.playlistmarket.ui.media_library.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmarket.databinding.FragmentPlaylistsBinding
import com.example.playlistmarket.ui.BindingFragment

class PlaylistsFragment: BindingFragment<FragmentPlaylistsBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
       return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    companion object{
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}