package com.jeandealmeida_dev.itunesplayer.ui.player

import com.jeandealmeida_dev.itunesplayer.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PlayerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel = PlayerViewModel()

    @Test
    fun `GIVEN ViewModel is created WHEN initial state is observed THEN all fields are at defaults`() {
        val state = viewModel.uiState.value

        assertNull(state.track)
        assertFalse(state.isPlaying)
        assertFalse(state.isPreparing)
        assertEquals(0L, state.positionMs)
        assertEquals(0L, state.durationMs)
        assertFalse(state.isRepeat)
    }

    @Test
    fun `GIVEN repeat is off WHEN toggleRepeat is called THEN isRepeat becomes true`() {
        viewModel.toggleRepeat()

        assertTrue(viewModel.uiState.value.isRepeat)
    }

    @Test
    fun `GIVEN repeat is on WHEN toggleRepeat is called again THEN isRepeat returns to false`() {
        viewModel.toggleRepeat()
        viewModel.toggleRepeat()

        assertFalse(viewModel.uiState.value.isRepeat)
    }

    @Test
    fun `GIVEN no media player initialized WHEN togglePlayPause is called THEN state does not change`() {
        val stateBefore = viewModel.uiState.value

        viewModel.togglePlayPause()

        assertEquals(stateBefore, viewModel.uiState.value)
    }

    @Test
    fun `GIVEN no media player initialized WHEN pause is called THEN state does not change`() {
        val stateBefore = viewModel.uiState.value

        viewModel.pause()

        assertEquals(stateBefore, viewModel.uiState.value)
    }

    @Test
    fun `GIVEN no media player initialized WHEN seekTo is called THEN state does not change`() {
        val stateBefore = viewModel.uiState.value

        viewModel.seekTo(0.5f)

        assertEquals(stateBefore, viewModel.uiState.value)
    }
}
