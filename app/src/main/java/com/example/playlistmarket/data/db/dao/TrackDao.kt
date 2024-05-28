package com.example.playlistmarket.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmarket.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(track: TrackEntity)
    @Delete
    suspend fun removeFromFavorites(track: TrackEntity)
    @Query("SELECT * FROM favorite_tracks")
    suspend fun getFavoritesTracks(): List<TrackEntity>
    @Query("SELECT track_id FROM favorite_tracks ")
    suspend fun getFavoritesTracksIds(): List<Int>
}