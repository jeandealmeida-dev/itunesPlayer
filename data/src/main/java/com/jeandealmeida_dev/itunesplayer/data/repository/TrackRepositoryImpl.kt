package com.jeandealmeida_dev.itunesplayer.data.repository

import com.jeandealmeida_dev.itunesplayer.data.network.NetworkModule
import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.toDomain
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository

class TrackRepositoryImpl(
    private val service: ItunesService = NetworkModule.itunesService,
) : TrackRepository {

    override suspend fun search(term: String): List<Track> =
        service.search(term).results.map { it.toDomain() }

    override suspend fun getTrack(id: Long): Track? =
        service.lookup(id).results.firstOrNull()?.toDomain()
}
