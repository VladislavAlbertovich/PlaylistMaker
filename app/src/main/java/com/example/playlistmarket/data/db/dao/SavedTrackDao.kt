package com.example.playlistmarket.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmarket.data.db.entity.SavedTrackEntity

@Dao
interface SavedTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun addItem(savedTrackEntity: SavedTrackEntity)
    @Delete
    suspend fun removeItem(savedTrackEntity: SavedTrackEntity)
    @Query("SELECT * FROM saved_track_table") suspend fun getAllItems(): List<SavedTrackEntity>
}