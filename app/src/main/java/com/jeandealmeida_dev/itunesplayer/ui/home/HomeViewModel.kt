package com.jeandealmeida_dev.itunesplayer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jeandealmeida_dev.itunesplayer.data.network.NetworkModule
import com.jeandealmeida_dev.itunesplayer.data.repository.TrackRepositoryImpl
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetPagedTracksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

data class HomeUiState(val query: String = "")

class HomeViewModel(
    private val getPagedTracks: GetPagedTracksUseCase = GetPagedTracksUseCase(
        TrackRepositoryImpl(NetworkModule.itunesService)
    ),
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val tracks: Flow<PagingData<Track>> = _uiState
        .map { it.query.ifBlank { "Daft Punk" } }
        .flatMapLatest { getPagedTracks(it) }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }
}
