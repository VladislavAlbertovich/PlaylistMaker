package com.example.playlistmarket.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmarket.data.db.dao.TrackDao
import com.example.playlistmarket.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class MediaLibraryDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}