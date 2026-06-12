package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetAlbumTracksUseCaseTest {

    private val repository: TrackRepository = mockk()
    private val useCase = GetAlbumTracksUseCase(repository)

    @Test
    fun `GIVEN a valid collectionId WHEN invoke is called THEN returns tracks for that album`() = runTest {
        val collectionId = 123L
        val tracks = listOf(
            aTrack(id = 1L, title = "Track 1", collectionId = collectionId),
            aTrack(id = 2L, title = "Track 2", collectionId = collectionId),
        )
        coEvery { repository.getAlbumTracks(collectionId) } returns tracks

        val result = useCase(collectionId)

        assertEquals(tracks, result)
        coVerify(exactly = 1) { repository.getAlbumTracks(collectionId) }
    }

    @Test
    fun `GIVEN no tracks for collectionId WHEN invoke is called THEN returns empty list`() = runTest {
        coEvery { repository.getAlbumTracks(any()) } returns emptyList()

        val result = useCase(999L)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN a collectionId WHEN invoke is called THEN delegates collectionId unmodified to repository`() = runTest {
        val collectionId = 42L
        coEvery { repository.getAlbumTracks(collectionId) } returns emptyList()

        useCase(collectionId)

        coVerify { repository.getAlbumTracks(collectionId) }
    }
}
