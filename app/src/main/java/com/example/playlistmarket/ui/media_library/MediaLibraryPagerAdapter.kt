package com.example.playlistmarket.ui.media_library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmarket.ui.media_library.favorite_tracks.FavoriteTracksFragment
import com.example.playlistmarket.ui.media_library.playlists.PlaylistsFragment

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount()= 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}