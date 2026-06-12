package com.jeandealmeida_dev.itunesplayer.ui.home

import com.jeandealmeida_dev.itunesplayer.MainDispatcherRule
import com.jeandealmeida_dev.itunesplayer.domain.usecase.GetPagedTracksUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getPagedTracks: GetPagedTracksUseCase = mockk()

    @Test
    fun `GIVEN ViewModel is created WHEN initial state is observed THEN query is empty`() = runTest {
        every { getPagedTracks(any()) } returns emptyFlow()
        val viewModel = HomeViewModel(getPagedTracks)
        assertEquals("", viewModel.uiState.value.query)
    }

    @Test
    fun `GIVEN a new query WHEN onQueryChange is called THEN query is reflected in state`() = runTest {
        every { getPagedTracks(any()) } returns emptyFlow()
        val viewModel = HomeViewModel(getPagedTracks)
        viewModel.onQueryChange("queen")
        assertEquals("queen", viewModel.uiState.value.query)
    }

    @Test
    fun `GIVEN multiple queries WHEN onQueryChange is called twice THEN last query is in state`() = runTest {
        every { getPagedTracks(any()) } returns emptyFlow()
        val viewModel = HomeViewModel(getPagedTracks)
        viewModel.onQueryChange("a")
        viewModel.onQueryChange("ab")
        assertEquals("ab", viewModel.uiState.value.query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN blank query WHEN tracks flow is collected THEN Daft Punk is used as the default search term`() = runTest {
        every { getPagedTracks(any()) } returns emptyFlow()
        val viewModel = HomeViewModel(getPagedTracks)

        val job = launch { viewModel.tracks.collect {} }
        advanceUntilIdle()
        job.cancel()

        verify { getPagedTracks("Daft Punk") }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN a non-blank query WHEN tracks flow is collected THEN getPagedTracks is called with that query`() = runTest {
        every { getPagedTracks(any()) } returns emptyFlow()
        val viewModel = HomeViewModel(getPagedTracks)
        viewModel.onQueryChange("queen")

        val job = launch { viewModel.tracks.collect {} }
        advanceUntilIdle()
        job.cancel()

        verify { getPagedTracks("queen") }
    }

    @Test
    fun `GIVEN a non-blank query is set WHEN onQueryChange is called with empty string THEN query resets to empty`() = runTest {
        every { getPagedTracks(any()) } returns emptyFlow()
        val viewModel = HomeViewModel(getPagedTracks)
        viewModel.onQueryChange("daft punk")

        viewModel.onQueryChange("")

        assertEquals("", viewModel.uiState.value.query)
    }
}
