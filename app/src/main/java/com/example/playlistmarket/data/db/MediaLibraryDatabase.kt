package com.example.playlistmarket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmarket.data.db.dao.PlaylistDao
import com.example.playlistmarket.data.db.dao.SavedTrackDao
import com.example.playlistmarket.data.db.dao.TrackDao
import com.example.playlistmarket.data.db.entity.PlaylistEntity
import com.example.playlistmarket.data.db.entity.SavedTrackEntity
import com.example.playlistmarket.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class, SavedTrackEntity::class])
abstract class MediaLibraryDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao

    abstract fun savedTrackDao(): SavedTrackDao
}