package com.example.playlistmarket

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val onTrackClickListener: OnTrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

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
    fun updateTracks(newTracks: ArrayList<Track>){
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }

    fun interface OnTrackClickListener{
        fun onTrackClick(track: Track)
    }
}