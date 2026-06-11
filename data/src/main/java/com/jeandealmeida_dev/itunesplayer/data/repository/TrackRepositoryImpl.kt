package com.jeandealmeida_dev.itunesplayer.data.repository

import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository

class TrackRepositoryImpl : TrackRepository {
    override suspend fun search(term: String): List<Track> = emptyList()
    override suspend fun getTrack(id: Long): Track? = null
}
