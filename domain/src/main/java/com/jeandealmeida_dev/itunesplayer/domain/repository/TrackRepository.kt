package com.jeandealmeida_dev.itunesplayer.domain.repository

import androidx.paging.PagingData
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun search(term: String): List<Track>
    suspend fun getTrack(id: Long): Track?
    fun searchPaged(term: String): Flow<PagingData<Track>>
}
