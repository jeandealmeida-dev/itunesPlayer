package com.jeandealmeida_dev.itunesplayer.data.remote.dto

import com.squareup.moshi.Json

data class SearchDto(
    @Json(name = "results") val results: List<TrackDto>,
    @Json(name = "resultCount") val resultCount: Int,
)
