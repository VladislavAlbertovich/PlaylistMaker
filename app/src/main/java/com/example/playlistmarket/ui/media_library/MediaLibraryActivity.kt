package com.example.playlistmarket.ui.media_library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator =TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            when(position){
                0 ->tab.text = getString(R.string.favorite_tracks)
                1 ->tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        binding.buttonBack.setOnClickListener{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}