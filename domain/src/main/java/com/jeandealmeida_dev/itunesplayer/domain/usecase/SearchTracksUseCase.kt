package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository

class SearchTracksUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(term: String): List<Track> = repository.search(term)
}
