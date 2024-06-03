package com.example.playlistmarket.ui.media_library.playlists

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.databinding.ItemPlaylistBinding
import com.example.playlistmarket.domain.media_library.models.Playlist

class PlaylistsAdapter(private val onItemClick: (playlist: Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    private var playlistsList: List<Playlist> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylistsList(playlistsList: List<Playlist>) {
        this.playlistsList = playlistsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            ItemPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ){ position: Int ->
            if (position!= RecyclerView.NO_POSITION){
                playlistsList.getOrNull(position)?.let{ playlist ->
                    onItemClick(playlist)
                }
            }
        }
    }

    override fun getItemCount() = playlistsList.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlistsList[position])
    }
}