package com.jeandealmeida_dev.itunesplayer.data.remote.dto

import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.squareup.moshi.Json

data class TrackDto(
    @Json(name = "trackId") val id: Long?,
    @Json(name = "trackName") val title: String?,
    @Json(name = "artistName") val artist: String?,
    @Json(name = "collectionName") val album: String?,
    @Json(name = "artworkUrl100") val artworkUrl: String?,
    @Json(name = "previewUrl") val previewUrl: String?,
    @Json(name = "trackPrice") val trackPrice: Double?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "trackTimeMillis") val durationMs: Long?,
)

fun TrackDto.toDomain() = Track(
    id = id ?: -1L,
    title = title.orEmpty(),
    artist = artist.orEmpty(),
    album = album.orEmpty(),
    artworkUrl = artworkUrl.orEmpty(),
    previewUrl = previewUrl.orEmpty(),
    trackPrice = trackPrice ?: 0.0,
    currency = currency.orEmpty(),
    durationMs = durationMs ?: 0L,
)
