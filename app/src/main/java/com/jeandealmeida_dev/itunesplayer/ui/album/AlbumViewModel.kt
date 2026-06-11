package com.jeandealmeida_dev.itunesplayer.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeandealmeida_dev.itunesplayer.data.network.NetworkModule
import com.jeandealmeida_dev.itunesplayer.data.repository.TrackRepositoryImpl
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetAlbumTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlbumUiState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
)

class AlbumViewModel(
    private val getAlbumTracks: GetAlbumTracksUseCase = GetAlbumTracksUseCase(
        TrackRepositoryImpl(NetworkModule.itunesService)
    ),
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    fun loadAlbum(collectionId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getAlbumTracks(collectionId) }
                .onSuccess { tracks -> _uiState.update { it.copy(tracks = tracks, isLoading = false) } }
                .onFailure { _uiState.update { it.copy(isLoading = false) } }
        }
    }
}
