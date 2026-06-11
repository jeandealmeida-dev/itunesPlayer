package com.jeandealmeida_dev.itunesplayer.domain.repository

import com.jeandealmeida_dev.itunesplayer.domain.model.Track

interface TrackRepository {
    suspend fun search(term: String): List<Track>
    suspend fun getTrack(id: Long): Track?
}
