package com.example.playlistmarket.ui.media_library.favorite_tracks

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteTrackViewHolder(favoriteTrackItemView: View) :
    RecyclerView.ViewHolder(favoriteTrackItemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val coverArtwork: ImageView = itemView.findViewById(R.id.coverArtwork)
    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        Glide
            .with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen._2dp)))
            .into(coverArtwork)
    }
}
