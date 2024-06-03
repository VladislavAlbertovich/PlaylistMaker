package com.example.playlistmarket.ui.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ItemPlaylistForBottomSheetBinding
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.utils.formatTextByNumbers

class PlayerPlaylistViewHolder(private val binding: ItemPlaylistForBottomSheetBinding,  onItemClick: (position: Int) -> Unit): RecyclerView.ViewHolder(binding.root) {


    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }
    fun bind (playlist: Playlist){
        binding.playlistTitle.text = playlist.title
        binding.tracksCount.text = formatTextByNumbers(playlist.tracksCount)

        Glide
            .with(itemView.context)
            .load(playlist.cover)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.playlistImage)
    }
}