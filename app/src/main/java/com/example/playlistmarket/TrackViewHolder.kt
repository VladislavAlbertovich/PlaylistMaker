package com.example.playlistmarket

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById<TextView>(R.id.trackName)
    private val artistName: TextView = itemView.findViewById<TextView>(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById<TextView>(R.id.trackTime)
    private val coverArtwork: ImageView = itemView.findViewById<ImageView>(R.id.coverArtwork)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide
            .with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen._2dp)))
            .into(coverArtwork)
    }
}