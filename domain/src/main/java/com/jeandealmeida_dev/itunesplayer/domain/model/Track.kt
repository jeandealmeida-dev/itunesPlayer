package com.jeandealmeida_dev.itunesplayer.domain.model

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val artworkUrl: String,
    val previewUrl: String,
    val trackPrice: Double,
    val currency: String,
)
