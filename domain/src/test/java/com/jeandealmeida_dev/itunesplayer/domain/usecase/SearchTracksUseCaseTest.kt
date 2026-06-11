package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchTracksUseCaseTest {

    private val repository: TrackRepository = mockk()
    private val useCase = SearchTracksUseCase(repository)

    @Test
    fun `GIVEN a valid term WHEN search is called THEN returns mapped tracks from repository`() = runTest {
        val tracks = listOf(aTrack(id = 1L, title = "Come Together"), aTrack(id = 2L, title = "Let It Be"))
        coEvery { repository.search("beatles") } returns tracks

        val result = useCase("beatles")

        assertEquals(tracks, result)
        coVerify(exactly = 1) { repository.search("beatles") }
    }

    @Test
    fun `GIVEN no results in repository WHEN search is called THEN returns empty list`() = runTest {
        coEvery { repository.search(any()) } returns emptyList()

        val result = useCase("unknownxyz")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN a search term WHEN search is called THEN delegates the term unmodified to repository`() = runTest {
        val term = "Taylor Swift"
        coEvery { repository.search(term) } returns emptyList()

        useCase(term)

        coVerify { repository.search(term) }
    }
}
