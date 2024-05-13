package com.example.playlistmarket.data.converters

import com.example.playlistmarket.data.db.entity.TrackEntity
import com.example.playlistmarket.domain.search.models.Track

class MediaLibraryDatabaseConverter {
    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.previewUrl,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100, trackEntity.trackId,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country)
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.previewUrl,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100, track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            System.currentTimeMillis())
    }
}