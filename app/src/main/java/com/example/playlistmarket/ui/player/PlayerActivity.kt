package com.example.playlistmarket.ui.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ActivityPlayerBinding
import com.example.playlistmarket.domain.search.models.Track
import com.example.playlistmarket.presentation.player.PlayerViewModel
import com.example.playlistmarket.ui.player.Model.PlayerScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val playerViewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerViewModel.observeTrack().observe(this){
            bind(it)
        }



        playerViewModel.observePlayerScreenState().observe(this) { state ->
            when (state) {
                is PlayerScreenState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                }

                is PlayerScreenState.Default -> {
                }

                is PlayerScreenState.Prepared -> {
                    binding.playButton.isEnabled = true
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.timeFragmentTextview.text = "00:00"
                    playerViewModel.stopUpdateTime()
                }

                is PlayerScreenState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)
                    playerViewModel.startUpdateTime()
                    playerViewModel.observeTime().observe(this) {
                        binding.timeFragmentTextview.text = it
                    }
                }
            }
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.playButton.setOnClickListener {
            playerViewModel.playbackControl()
        }
    }

    private fun bind(track: Track) {
        binding.apply {
            trackName.text = track?.trackName
            artistName.text = track?.artistName
            durationValueTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
            if (track?.collectionName.isNullOrEmpty()) {
                binding.albumValueTextView.text = track?.collectionName
            } else {
                binding.albumValueTextView.text = "-"
            }
            yearValueTextView.text =
                SimpleDateFormat("yyyy", Locale.getDefault()).format(track?.releaseDate)
            genreValueTextView.text = track?.primaryGenreName
            countryValueTextView.text = track?.country
            getCoverArtwork(track)
        }
    }

    private fun getCoverArtwork(track: Track) {
        Glide
            .with(this)
            .load(track?.artworkUrl512())
            .placeholder(R.drawable.placeholder_album_in_player)
            .centerCrop()
            .transform(
                RoundedCorners(
                    binding.albumImageView.context.resources.getDimensionPixelOffset(
                        R.dimen._8dp
                    )
                )
            )
            .into(binding.albumImageView)
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.onReset()
        playerViewModel.preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }

    override fun onDestroy() {
        playerViewModel.onDestroy()
        super.onDestroy()
    }
}

