package com.jeandealmeida_dev.itunesplayer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeandealmeida_dev.itunesplayer.data.network.NetworkModule
import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.toDomain
import com.jeandealmeida_dev.itunesplayer.data.remote.paging.TrackPagingSource
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class TrackRepositoryImpl(
    private val service: ItunesService = NetworkModule.itunesService,
) : TrackRepository {

    override suspend fun search(term: String): List<Track> =
        service.search(term).results.map { it.toDomain() }

    override suspend fun getTrack(id: Long): Track? =
        service.lookup(id).results.firstOrNull()?.toDomain()

    override fun searchPaged(term: String): Flow<PagingData<Track>> = Pager(
        config = PagingConfig(pageSize = TrackPagingSource.PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { TrackPagingSource(service, term) },
    ).flow

    override suspend fun getAlbumTracks(collectionId: Long): List<Track> =
        service.lookup(collectionId).results
            .filter { it.id != null }
            .map { it.toDomain() }
}
