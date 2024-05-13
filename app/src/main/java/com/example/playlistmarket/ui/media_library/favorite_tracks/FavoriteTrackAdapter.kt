package com.example.playlistmarket.ui.media_library.favorite_tracks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.search.models.Track

class FavoriteTrackAdapter(private val onFavoriteTrackClickListener: OnFavoriteTrackClickListener): RecyclerView.Adapter<FavoriteTrackViewHolder>() {
    private val favoriteTracks = ArrayList<Track>()

    fun interface OnFavoriteTrackClickListener{
        fun onFavoriteTrackClick(favoriteTrack: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return FavoriteTrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteTracks.size
    }

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int) {
        holder.bind(favoriteTracks[position])
        holder.itemView.setOnClickListener {
            onFavoriteTrackClickListener.onFavoriteTrackClick(favoriteTracks[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: List<Track>){
        favoriteTracks.clear()
        favoriteTracks.addAll(newTracks)
        this.notifyDataSetChanged()
    }
}