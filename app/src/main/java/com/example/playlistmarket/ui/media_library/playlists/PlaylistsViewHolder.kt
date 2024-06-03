package com.example.playlistmarket.ui.media_library.playlists

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ItemPlaylistBinding
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.utils.formatTextByNumbers

class PlaylistsViewHolder(private val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind (playlist: Playlist){
        binding.playlistTitle.text = playlist.title
        binding.tracksCount.text = formatTextByNumbers(playlist.tracksCount)

        Glide
            .with(itemView.context)
            .load(playlist.cover)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.playlistCover)
    }
}