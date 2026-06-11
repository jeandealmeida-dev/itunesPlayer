package com.jeandealmeida_dev.itunesplayer.ui.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlayerUiState(
    val track: Track? = null,
    val isPlaying: Boolean = false,
    val isPreparing: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isRepeat: Boolean = false,
)

class PlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var isPrepared = false
    private var tickerJob: Job? = null

    fun setTrack(track: Track) {
        if (_uiState.value.track?.id == track.id) return
        releasePlayer()
        _uiState.value = PlayerUiState(
            track = track,
            isPreparing = true,
            durationMs = track.durationMs,
        )
        preparePlayer(track.previewUrl, track.durationMs)
    }

    fun togglePlayPause() {
        val player = mediaPlayer ?: return
        if (!isPrepared) return
        if (player.isPlaying) {
            player.pause()
            tickerJob?.cancel()
            _uiState.update { it.copy(isPlaying = false) }
        } else {
            player.start()
            _uiState.update { it.copy(isPlaying = true) }
            startTicker()
        }
    }

    fun seekTo(fraction: Float) {
        val player = mediaPlayer ?: return
        if (!isPrepared) return
        val duration = player.duration.takeIf { it > 0 } ?: return
        val targetMs = (fraction * duration).toInt()
        player.seekTo(targetMs)
        _uiState.update { it.copy(positionMs = targetMs.toLong()) }
    }

    fun toggleRepeat() {
        _uiState.update { it.copy(isRepeat = !it.isRepeat) }
    }

    fun pause() {
        val player = mediaPlayer ?: return
        if (isPrepared && player.isPlaying) {
            player.pause()
            tickerJob?.cancel()
            _uiState.update { it.copy(isPlaying = false) }
        }
    }

    private fun preparePlayer(url: String, fallbackDurationMs: Long) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            setOnPreparedListener { mp ->
                isPrepared = true
                val actualDuration = mp.duration.toLong().takeIf { it > 0 } ?: fallbackDurationMs
                _uiState.update { it.copy(isPreparing = false, isPlaying = true, durationMs = actualDuration) }
                mp.start()
                startTicker()
            }
            setOnCompletionListener { mp ->
                if (_uiState.value.isRepeat) {
                    mp.seekTo(0)
                    mp.start()
                    _uiState.update { it.copy(positionMs = 0L) }
                    startTicker()
                } else {
                    tickerJob?.cancel()
                    _uiState.update { it.copy(isPlaying = false, positionMs = 0L) }
                }
            }
            setOnErrorListener { _, _, _ ->
                _uiState.update { it.copy(isPreparing = false, isPlaying = false) }
                true
            }
            prepareAsync()
        }
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(500L)
                val player = mediaPlayer ?: break
                if (!isPrepared || !player.isPlaying) break
                _uiState.update { it.copy(positionMs = player.currentPosition.toLong()) }
            }
        }
    }

    private fun releasePlayer() {
        tickerJob?.cancel()
        isPrepared = false
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
