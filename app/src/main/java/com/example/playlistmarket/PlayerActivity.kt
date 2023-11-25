package com.example.playlistmarket

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.SearchActivity.Companion.TRACK
import com.example.playlistmarket.databinding.ActivityMainBinding
import com.example.playlistmarket.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import org.w3c.dom.Text
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Date
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getTrack()
        bind()

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun bind() {
        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            durationValueTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
            if (track.collectionName.isNullOrEmpty()){
                binding.albumValueTextView.text = track.collectionName
            } else {
                binding.albumValueTextView.text = "-"
            }
            yearValueTextView.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(track.releaseDate)
            genreValueTextView.text = track.primaryGenreName
            countryValueTextView.text = track.country
            getCoverArtwork()
        }
    }

    private fun getTrack(): Track {
        val json = intent.getStringExtra(TRACK)
        return Gson().fromJson(json, Track::class.java)
    }

    private fun getCoverArtwork(){
        Glide
            .with(this)
            .load(getCoverArtworkHighQuality(track))
            .placeholder(R.drawable.placeholder_album_in_player)
            .centerCrop()
            .transform(RoundedCorners(binding.albumImageView.context.resources.getDimensionPixelOffset(R.dimen._8dp)))
            .into(binding.albumImageView)
    }
    private fun getCoverArtworkHighQuality(track: Track) = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}