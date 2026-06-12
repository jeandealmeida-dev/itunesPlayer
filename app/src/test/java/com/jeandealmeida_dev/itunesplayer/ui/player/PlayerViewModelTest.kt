package com.jeandealmeida_dev.itunesplayer.ui.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.jeandealmeida_dev.itunesplayer.MainDispatcherRule
import com.jeandealmeida_dev.itunesplayer.aTrack
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlayerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // ── Tests that need no media player ──────────────────────────────────────

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

    @Test
    fun `GIVEN no media player initialized WHEN seekTo is called with boundary value 1f THEN state does not change`() {
        val stateBefore = viewModel.uiState.value

        viewModel.seekTo(1f)

        assertEquals(stateBefore, viewModel.uiState.value)
    }

    @Test
    fun `GIVEN toggleRepeat is called three times WHEN isRepeat is observed THEN isRepeat is true`() {
        viewModel.toggleRepeat()
        viewModel.toggleRepeat()
        viewModel.toggleRepeat()

        assertTrue(viewModel.uiState.value.isRepeat)
    }

    @Test
    fun `GIVEN toggleRepeat is called four times WHEN isRepeat is observed THEN isRepeat is false`() {
        viewModel.toggleRepeat()
        viewModel.toggleRepeat()
        viewModel.toggleRepeat()
        viewModel.toggleRepeat()

        assertFalse(viewModel.uiState.value.isRepeat)
    }

    // ── Tests that inject a mock MediaPlayer ─────────────────────────────────

    private val mockPlayer: MediaPlayer = mockk(relaxed = true)
    private val preparedSlot = slot<MediaPlayer.OnPreparedListener>()
    private val completionSlot = slot<MediaPlayer.OnCompletionListener>()
    private val errorSlot = slot<MediaPlayer.OnErrorListener>()

    @Before
    fun setUpAudioAttributesMock() {
        mockkConstructor(AudioAttributes.Builder::class)
        every { anyConstructed<AudioAttributes.Builder>().setContentType(any()) } answers { self as AudioAttributes.Builder }
        every { anyConstructed<AudioAttributes.Builder>().setUsage(any()) } answers { self as AudioAttributes.Builder }
        every { anyConstructed<AudioAttributes.Builder>().build() } returns mockk(relaxed = true)
    }

    @After
    fun tearDownAudioAttributesMock() {
        unmockkConstructor(AudioAttributes.Builder::class)
    }

    private fun setupViewModel(): PlayerViewModel {
        every { mockPlayer.setOnPreparedListener(capture(preparedSlot)) } just Runs
        every { mockPlayer.setOnCompletionListener(capture(completionSlot)) } just Runs
        every { mockPlayer.setOnErrorListener(capture(errorSlot)) } just Runs
        return PlayerViewModel(mediaPlayerFactory = { mockPlayer })
    }

    private fun PlayerViewModel.firePrepared() {
        preparedSlot.captured.onPrepared(mockPlayer)
    }

    @Test
    fun `GIVEN a track WHEN setTrack is called THEN state shows isPreparing true with the track and its durationMs`() {
        val vm = setupViewModel()
        val track = aTrack(id = 1L, durationMs = 180_000L)

        vm.setTrack(track)

        with(vm.uiState.value) {
            assertEquals(track, this.track)
            assertTrue(isPreparing)
            assertEquals(180_000L, durationMs)
        }
    }

    @Test
    fun `GIVEN a track is set WHEN setTrack is called with the same id THEN player is not released`() {
        val vm = setupViewModel()
        val track = aTrack(id = 1L)
        vm.setTrack(track)

        vm.setTrack(track)

        verify(exactly = 0) { mockPlayer.release() }
    }

    @Test
    fun `GIVEN a track is loaded WHEN setTrack is called with a different id THEN previous player is released`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack(id = 1L))

        vm.setTrack(aTrack(id = 2L))

        verify(exactly = 1) { mockPlayer.release() }
    }

    @Test
    fun `GIVEN setTrack is called WHEN prepared callback fires THEN isPreparing becomes false and isPlaying becomes true`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())

        vm.firePrepared()

        assertFalse(vm.uiState.value.isPreparing)
        assertTrue(vm.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN player reports a duration WHEN prepared callback fires THEN durationMs is updated from the player`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack(durationMs = 30_000L))
        every { mockPlayer.duration } returns 28_500

        vm.firePrepared()

        assertEquals(28_500L, vm.uiState.value.durationMs)
    }

    @Test
    fun `GIVEN player is prepared and playing WHEN togglePlayPause is called THEN player pauses and isPlaying becomes false`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        every { mockPlayer.isPlaying } returns true
        vm.firePrepared()

        vm.togglePlayPause()

        verify { mockPlayer.pause() }
        assertFalse(vm.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN player is prepared and not playing WHEN togglePlayPause is called THEN player starts and isPlaying becomes true`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        every { mockPlayer.isPlaying } returns false
        vm.firePrepared()

        vm.togglePlayPause()

        verify(atLeast = 1) { mockPlayer.start() }
        assertTrue(vm.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN player is prepared WHEN seekTo is called THEN player seeks to the correct position and positionMs updates`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        every { mockPlayer.duration } returns 100_000
        vm.firePrepared()

        vm.seekTo(0.5f)

        verify { mockPlayer.seekTo(50_000) }
        assertEquals(50_000L, vm.uiState.value.positionMs)
    }

    @Test
    fun `GIVEN player is prepared and playing WHEN pause is called THEN player pauses and isPlaying becomes false`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        every { mockPlayer.isPlaying } returns true
        vm.firePrepared()

        vm.pause()

        verify { mockPlayer.pause() }
        assertFalse(vm.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN player is prepared and not playing WHEN pause is called THEN player is not paused again`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        every { mockPlayer.isPlaying } returns false
        vm.firePrepared()

        vm.pause()

        verify(exactly = 0) { mockPlayer.pause() }
    }

    @Test
    fun `GIVEN player encounters an error WHEN error callback fires THEN isPreparing and isPlaying become false`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())

        errorSlot.captured.onError(mockPlayer, 0, 0)

        assertFalse(vm.uiState.value.isPreparing)
        assertFalse(vm.uiState.value.isPlaying)
    }

    @Test
    fun `GIVEN repeat is off WHEN completion callback fires THEN isPlaying becomes false and positionMs resets to zero`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        vm.firePrepared()

        completionSlot.captured.onCompletion(mockPlayer)

        assertFalse(vm.uiState.value.isPlaying)
        assertEquals(0L, vm.uiState.value.positionMs)
    }

    @Test
    fun `GIVEN repeat is on WHEN completion callback fires THEN player seeks to zero and restarts`() {
        val vm = setupViewModel()
        vm.setTrack(aTrack())
        vm.toggleRepeat()
        vm.firePrepared()

        completionSlot.captured.onCompletion(mockPlayer)

        verify { mockPlayer.seekTo(0) }
        verify(atLeast = 2) { mockPlayer.start() } // once from prepared, once from completion
        assertEquals(0L, vm.uiState.value.positionMs)
    }
}
