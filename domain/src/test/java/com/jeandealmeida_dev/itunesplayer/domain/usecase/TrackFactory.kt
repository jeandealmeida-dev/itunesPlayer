package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.model.Track

fun aTrack(
    id: Long = 1L,
    title: String = "Track Title",
    artist: String = "Artist Name",
    album: String = "Album Name",
    artworkUrl: String = "https://example.com/art.jpg",
    previewUrl: String = "https://example.com/preview.m4a",
    trackPrice: Double = 1.29,
    currency: String = "USD",
    durationMs: Long = 240_000L,
) = Track(
    id = id,
    title = title,
    artist = artist,
    album = album,
    artworkUrl = artworkUrl,
    previewUrl = previewUrl,
    trackPrice = trackPrice,
    currency = currency,
    durationMs = durationMs,
)
