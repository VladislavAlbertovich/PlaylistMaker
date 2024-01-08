package com.example.playlistmarket.ui.player

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.Creator
import com.example.playlistmarket.R
import com.example.playlistmarket.data.TRACKS_HISTORY_SHARED_PREFERENCES_KEY
import com.example.playlistmarket.domain.callbacks.PausePlayerListener
import com.example.playlistmarket.domain.callbacks.PlayerOnCompletionListener
import com.example.playlistmarket.domain.callbacks.PlayerOnPreparedListener
import com.example.playlistmarket.domain.callbacks.StartPlayerListener
import com.example.playlistmarket.domain.callbacks.TimeFragmentListener
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    private var track: Track? = null
    private lateinit var binding: ActivityPlayerBinding
    private val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val mediaPlayerUseCase = Creator.provideMediaPlayerUseCase()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)

        track = Creator.getTrackUseCase(sharedPreferences).execute()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bind()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        mediaPlayerUseCase.preparePlayer(
            track,
            playerOnPreparedListener(),
            playerOnCompletionListener()
        )

        binding.playButton.setOnClickListener {
            mediaPlayerUseCase.playbackControl(
                startPlayerListener(),
                timeFragmentListener(),
                pausePlayerListener()
            )
        }
    }


    private fun playerOnPreparedListener() =
        object : PlayerOnPreparedListener {
            override fun listen() {
                binding.playButton.isEnabled = true
            }
        }


    private fun playerOnCompletionListener() = object : PlayerOnCompletionListener {
        override fun listen() {
            binding.playButton.setImageResource(R.drawable.play_button)
        }
    }

    private fun startPlayerListener() =
        object : StartPlayerListener {
            override fun listen() {
                binding.playButton.setImageResource(R.drawable.pause_button)
            }
        }

    private fun pausePlayerListener() =
        object : PausePlayerListener {
            override fun listen() {
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }

    private fun timeFragmentListener() = object : TimeFragmentListener {
        override fun listen() {
            binding.timeFragmentTextview.text =
                simpleDateFormat.format(mediaPlayerUseCase.getCurrentPosition())
        }
    }

    private fun bind() {
        binding.apply {
            trackName.text = track?.trackName
            artistName.text = track?.artistName
            durationValueTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTime)
            if (track?.collectionName.isNullOrEmpty()) {
                binding.albumValueTextView.text = track?.collectionName
            } else {
                binding.albumValueTextView.text = "-"
            }
            yearValueTextView.text =
                SimpleDateFormat("yyyy", Locale.getDefault()).format(track?.releaseDate)
            genreValueTextView.text = track?.primaryGenreName
            countryValueTextView.text = track?.country
            getCoverArtwork()
        }
    }

    private fun getCoverArtwork() {
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

    override fun onPause() {
        super.onPause()
        mediaPlayerUseCase.pausePlayer(pausePlayerListener())
        mediaPlayerUseCase.handlerRemoveCallbacks(timeFragmentListener())
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerUseCase.playerDestroy(timeFragmentListener())
    }
}

