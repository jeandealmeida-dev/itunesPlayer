package com.jeandealmeida_dev.itunesplayer.ui.album

import com.jeandealmeida_dev.itunesplayer.MainDispatcherRule
import com.jeandealmeida_dev.itunesplayer.aTrack
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetAlbumTracksUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AlbumViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAlbumTracks: GetAlbumTracksUseCase = mockk()

    @Test
    fun `GIVEN ViewModel is created WHEN initial state is observed THEN tracks is empty and isLoading is false`() {
        val viewModel = AlbumViewModel(getAlbumTracks)

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.tracks.isEmpty())
    }

    @Test
    fun `GIVEN a valid collectionId WHEN loadAlbum succeeds THEN tracks are updated and isLoading is false`() = runTest {
        val tracks = listOf(aTrack(id = 1L, title = "Come Together"), aTrack(id = 2L, title = "Let It Be"))
        coEvery { getAlbumTracks(123L) } returns tracks
        val viewModel = AlbumViewModel(getAlbumTracks)

        viewModel.loadAlbum(123L)

        assertEquals(tracks, viewModel.uiState.value.tracks)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `GIVEN repository throws WHEN loadAlbum fails THEN isLoading is false and tracks remain empty`() = runTest {
        coEvery { getAlbumTracks(any()) } throws RuntimeException("network error")
        val viewModel = AlbumViewModel(getAlbumTracks)

        viewModel.loadAlbum(1L)

        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.tracks.isEmpty())
    }

    @Test
    fun `GIVEN a second loadAlbum call WHEN first results are already loaded THEN tracks are replaced with new results`() = runTest {
        val firstBatch = listOf(aTrack(id = 1L))
        val secondBatch = listOf(aTrack(id = 2L), aTrack(id = 3L))
        coEvery { getAlbumTracks(1L) } returns firstBatch
        coEvery { getAlbumTracks(2L) } returns secondBatch
        val viewModel = AlbumViewModel(getAlbumTracks)

        viewModel.loadAlbum(1L)
        viewModel.loadAlbum(2L)

        assertEquals(secondBatch, viewModel.uiState.value.tracks)
    }
}
