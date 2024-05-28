package com.example.playlistmarket.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmarket.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun addItem(playlistEntity: PlaylistEntity)
    @Update suspend fun updateItem(playlistEntity: PlaylistEntity)
    @Query("SELECT * FROM playlist_table WHERE id = :id") suspend fun getItem(id: Int): PlaylistEntity
    @Query("SELECT * FROM playlist_table") suspend fun getAllItems(): List<PlaylistEntity>
    @Query("DELETE FROM playlist_table") suspend fun deleteAllItems()
    @Query("DELETE FROM playlist_table WHERE id = :id") suspend fun deletePlaylist(id: Int)
}