package com.example.playlistmarket

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.SearchActivity.Companion.TRACK
import com.example.playlistmarket.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    companion object {
        private const val DELAY = 500L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }


    private lateinit var handler: Handler
    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        track = getTrack()

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bind()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        preparePlayer()

        binding.playButton.setOnClickListener { playbackControl() }
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

    private fun getTrack(): Track {
        val json = intent.getStringExtra(TRACK)
        return Gson().fromJson(json, Track::class.java)
    }

    private fun getCoverArtwork() {
        Glide
            .with(this)
            .load(getCoverArtworkHighQuality(track))
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

    private fun getCoverArtworkHighQuality(track: Track) =
        track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            handler.removeCallbacks(createUpdateTimeFragmentTask())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        handler.postDelayed(createUpdateTimeFragmentTask(), DELAY)
    }

    private fun createUpdateTimeFragmentTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    binding.timeFragmentTextview.text =
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, DELAY)
                }

                if (playerState == STATE_PAUSED) {
                    handler.removeCallbacks(this)
                }

                if (playerState == STATE_PREPARED) {
                    binding.timeFragmentTextview.text = "00:00"
                    handler.removeCallbacks(this)
                }

            }
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
            STATE_PLAYING -> pausePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacks(createUpdateTimeFragmentTask())
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        playerState = STATE_DEFAULT
        handler.removeCallbacks(createUpdateTimeFragmentTask())
    }
}

