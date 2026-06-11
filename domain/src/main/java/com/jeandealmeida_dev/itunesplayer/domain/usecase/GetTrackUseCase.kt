package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository

class GetTrackUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(id: Long): Track? = repository.getTrack(id)
}
