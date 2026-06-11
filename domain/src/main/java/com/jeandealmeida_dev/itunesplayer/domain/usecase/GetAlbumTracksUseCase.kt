package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository

class GetAlbumTracksUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(collectionId: Long): List<Track> =
        repository.getAlbumTracks(collectionId)
}
