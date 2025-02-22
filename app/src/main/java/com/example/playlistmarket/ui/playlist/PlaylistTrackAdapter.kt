package com.example.playlistmarket.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.databinding.TrackViewBinding
import com.example.playlistmarket.domain.search.models.Track

class PlaylistTrackAdapter(
    private val onItemClick: (track: Track) -> Unit,
    private val onLongItemClick: (track: Track) -> Unit
): RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    private var trackList: List<Track> = emptyList()

    fun setTracks(tracks: List<Track>) {
        trackList = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackViewBinding.inflate(layoutInflater, parent, false)
        return PlaylistTrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        trackList.getOrNull(position)?.let { track ->
            holder.bind(track, onItemClick, onLongItemClick)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}