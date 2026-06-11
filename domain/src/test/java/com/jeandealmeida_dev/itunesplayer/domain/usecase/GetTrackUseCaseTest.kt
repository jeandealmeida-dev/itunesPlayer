package com.jeandealmeida_dev.itunesplayer.domain.usecase

import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetTrackUseCaseTest {

    private val repository: TrackRepository = mockk()
    private val useCase = GetTrackUseCase(repository)

    @Test
    fun `GIVEN an existing track id WHEN getTrack is called THEN returns the matching track`() = runTest {
        val track = aTrack(id = 42L, title = "Bohemian Rhapsody")
        coEvery { repository.getTrack(42L) } returns track

        val result = useCase(42L)

        assertEquals(track, result)
        coVerify(exactly = 1) { repository.getTrack(42L) }
    }

    @Test
    fun `GIVEN a non-existent track id WHEN getTrack is called THEN returns null`() = runTest {
        coEvery { repository.getTrack(any()) } returns null

        val result = useCase(99L)

        assertNull(result)
    }

    @Test
    fun `GIVEN a track id WHEN getTrack is called THEN delegates the id unmodified to repository`() = runTest {
        val id = 7L
        coEvery { repository.getTrack(id) } returns null

        useCase(id)

        coVerify { repository.getTrack(id) }
    }
}
