package com.example.playlistmarket.ui.playlist

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.TrackViewBinding
import com.example.playlistmarket.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistTrackViewHolder(
    private val binding: TrackViewBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        track: Track,
        onItemClick: (track: Track) -> Unit,
        onLongItemClick: (track: Track) -> Unit
    ) {
        binding.root.setOnClickListener { onItemClick(track) }
        binding.root.setOnLongClickListener {
            onLongItemClick(track)
            true
        }

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format((track.trackTimeMillis))

        Glide
            .with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen._2dp)))
            .into(binding.coverArtwork)
    }
}