package com.example.playlistmarket.ui.player

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.Creator
import com.example.playlistmarket.R
import com.example.playlistmarket.data.TRACKS_HISTORY_SHARED_PREFERENCES_KEY
import com.example.playlistmarket.domain.api.MediaPlayerRepository.StartPlayerListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.PausePlayerListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.TimeFragmentListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.PlayerOnCompletionListener
import com.example.playlistmarket.domain.api.MediaPlayerRepository.PlayerOnPreparedListener
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.databinding.ActivityPlayerBinding
import com.example.playlistmarket.domain.api.MediaPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private lateinit var player: MediaPlayerRepository

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            getSharedPreferences(TRACKS_HISTORY_SHARED_PREFERENCES_KEY, MODE_PRIVATE)

        player = Creator.getMediaPlayerUseCase().execute()
        track = Creator.getTrackUseCase(sharedPreferences).execute()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bind()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        player.preparePlayer(track, playerOnPreparedListener(), playerOnCompletionListener())

        binding.playButton.setOnClickListener {
            player.playbackControl(
                startPlayerListener(),
                timeFragmentListener(),
                pausePlayerListener()
            )
        }
    }

    private fun playerOnPreparedListener() =
        PlayerOnPreparedListener { binding.playButton.isEnabled = true }

    private fun playerOnCompletionListener() = PlayerOnCompletionListener {
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun startPlayerListener() =
        StartPlayerListener { binding.playButton.setImageResource(R.drawable.pause_button) }

    private fun pausePlayerListener() =
        PausePlayerListener { binding.playButton.setImageResource(R.drawable.play_button) }

    private fun timeFragmentListener() = TimeFragmentListener {
        binding.timeFragmentTextview.text =
            simpleDateFormat.format(player.getCurrentPosition())
    }

    private fun bind() {
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            durationValueTextView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            if (track.collectionName.isNullOrEmpty()) {
                binding.albumValueTextView.text = track.collectionName
            } else {
                binding.albumValueTextView.text = "-"
            }
            yearValueTextView.text =
                SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
            genreValueTextView.text = track.primaryGenreName
            countryValueTextView.text = track.country
            getCoverArtwork()
        }
    }

    private fun getCoverArtwork() {
        Glide
            .with(this)
            .load(track.artworkUrl512())
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
        player.pausePlayer(pausePlayerListener())
        player.handlerRemoveCallbacks(timeFragmentListener())
    }

    override fun onDestroy() {
        super.onDestroy()
        player.playerDestroy(timeFragmentListener())
    }
}

