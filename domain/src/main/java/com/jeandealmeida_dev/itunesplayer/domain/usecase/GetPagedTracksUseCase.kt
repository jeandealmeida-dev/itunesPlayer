package com.jeandealmeida_dev.itunesplayer.domain.usecase

import androidx.paging.PagingData
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class GetPagedTracksUseCase(private val repository: TrackRepository) {
    operator fun invoke(term: String): Flow<PagingData<Track>> = repository.searchPaged(term)
}
