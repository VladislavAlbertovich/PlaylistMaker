package com.example.playlistmarket.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.search.models.Track

class TrackAdapter(private val onTrackClickListener: OnTrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    private val tracks: ArrayList<Track> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{ onTrackClickListener.onTrackClick(tracks[position])}
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: List<Track>){

        tracks.clear()
        tracks.addAll(newTracks)
        this.notifyDataSetChanged()
    }

    fun interface OnTrackClickListener{
        fun onTrackClick(track: Track)
    }
}