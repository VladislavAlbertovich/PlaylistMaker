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
import com.google.gson.Gson
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var albumImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var durationTrack: TextView
    private lateinit var albumName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
        albumImage = findViewById(R.id.album_imageView)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        durationTrack = findViewById(R.id.duration_value_text_view)
        albumName = findViewById(R.id.album_value_text_view)
        year = findViewById(R.id.year_value_text_view)
        genre = findViewById(R.id.genre_value_text_view)
        country = findViewById(R.id.country_value_text_view)
        track = getTrack()
        bind()

        buttonBack.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bind() {
        trackName.text = track.trackName
        artistName.text = track.artistName
        durationTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        if (track.collectionName.isNullOrEmpty()){
            albumName.text = track.collectionName
        } else {
            albumName.text = "-"
        }
        year.text = track.releaseDate
        genre.text = track.primaryGenreName
        country.text = track.country
        getCoverArtwork()
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
            .transform(RoundedCorners(albumImage.context.resources.getDimensionPixelOffset(R.dimen._8dp)))
            .into(albumImage)
    }
    private fun getCoverArtworkHighQuality(track: Track) = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}