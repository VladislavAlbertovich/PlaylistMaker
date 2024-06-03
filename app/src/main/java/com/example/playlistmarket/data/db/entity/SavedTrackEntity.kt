package com.example.playlistmarket.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "saved_track_table")
@TypeConverters(DateConverter::class)
data class SavedTrackEntity(
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: Int,
    val previewUrl: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: Date,
    val primaryGenreName: String,
    val country: String,
    var timeOfAddingOfToFavorites: Long = 0
)