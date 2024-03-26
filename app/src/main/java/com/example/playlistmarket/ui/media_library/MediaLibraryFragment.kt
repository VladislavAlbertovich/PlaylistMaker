package com.example.playlistmarket.ui.media_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentMediaLibraryBinding
import com.example.playlistmarket.ui.BindingFragment
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment: BindingFragment<FragmentMediaLibraryBinding>() {

    private lateinit var tabMediator: TabLayoutMediator
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMediaLibraryBinding {
        return FragmentMediaLibraryBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)
        tabMediator =TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            when(position){
                0 ->tab.text = getString(R.string.favorite_tracks)
                1 ->tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}