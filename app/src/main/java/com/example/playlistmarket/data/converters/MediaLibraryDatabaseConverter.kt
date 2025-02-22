package com.example.playlistmarket.data.converters

import com.example.playlistmarket.data.db.entity.PlaylistEntity
import com.example.playlistmarket.data.db.entity.SavedTrackEntity
import com.example.playlistmarket.data.db.entity.TrackEntity
import com.example.playlistmarket.domain.media_library.models.Playlist
import com.example.playlistmarket.domain.search.models.Track

class MediaLibraryDatabaseConverter {
    fun map(trackEntity: TrackEntity) = Track(
        trackEntity.previewUrl,
        trackEntity.trackName,
        trackEntity.artistName,
        trackEntity.trackTimeMillis,
        trackEntity.artworkUrl100, trackEntity.trackId,
        trackEntity.collectionName,
        trackEntity.releaseDate,
        trackEntity.primaryGenreName,
        trackEntity.country
    )

    fun map(track: Track) =
        TrackEntity(
            track.trackId,
            track.previewUrl!!,
            track.trackName,
            track.artistName,
            track.trackTimeMillis, track.artworkUrl100,
            track.collectionName,
            track.releaseDate!!,
            track.primaryGenreName,
            track.country,
            System.currentTimeMillis()
        )

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            cover = playlist.cover,
            tracksIds = playlist.tracksIds,
            countOfTracks = playlist.tracksCount
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            description = playlistEntity.description,
            cover = playlistEntity.cover,
            tracksIds = playlistEntity.tracksIds,
            tracksCount = playlistEntity.countOfTracks
        )
    }

    fun mapToSavedTrackEntity(track: Track): SavedTrackEntity {
        return SavedTrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl!!,
            timeOfAddingOfToFavorites = System.currentTimeMillis()
        )
    }

    fun map(trackEntity: SavedTrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl
        )
    }

}