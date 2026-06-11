package com.jeandealmeida_dev.itunesplayer.data.repository

import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.SearchDto
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.TrackDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TrackRepositoryImplTest {

    private val service: ItunesService = mockk()
    private val repository = TrackRepositoryImpl(service)

    @Test
    fun `GIVEN a search term WHEN search is called THEN delegates to service and maps results`() = runTest {
        coEvery { service.search("beatles") } returns SearchDto(listOf(aTrackDto(id = 1L, title = "Yesterday")), 1)

        val result = repository.search("beatles")

        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("Yesterday", result[0].title)
        coVerify(exactly = 1) { service.search("beatles") }
    }

    @Test
    fun `GIVEN a search term WHEN search returns multiple results THEN all are mapped`() = runTest {
        val dtos = listOf(aTrackDto(id = 1L), aTrackDto(id = 2L), aTrackDto(id = 3L))
        coEvery { service.search(any()) } returns SearchDto(dtos, 3)

        val result = repository.search("query")

        assertEquals(3, result.size)
    }

    @Test
    fun `GIVEN a track id WHEN getTrack is called and lookup returns a result THEN returns mapped track`() = runTest {
        coEvery { service.lookup(42L) } returns SearchDto(listOf(aTrackDto(id = 42L, title = "Bohemian Rhapsody")), 1)

        val result = repository.getTrack(42L)

        assertNotNull(result)
        assertEquals(42L, result!!.id)
        assertEquals("Bohemian Rhapsody", result.title)
        coVerify(exactly = 1) { service.lookup(42L) }
    }

    @Test
    fun `GIVEN a track id WHEN lookup returns empty results THEN returns null`() = runTest {
        coEvery { service.lookup(any()) } returns SearchDto(emptyList(), 0)

        val result = repository.getTrack(99L)

        assertNull(result)
    }

    @Test
    fun `GIVEN a track id WHEN getTrack is called THEN delegates the id unmodified to service`() = runTest {
        val id = 7L
        coEvery { service.lookup(id) } returns SearchDto(emptyList(), 0)

        repository.getTrack(id)

        coVerify { service.lookup(id) }
    }
}

private fun aTrackDto(
    id: Long = 1L,
    title: String = "Track Title",
    artist: String = "Artist",
    album: String = "Album",
    artworkUrl: String = "https://example.com/art.jpg",
    previewUrl: String = "https://example.com/preview.m4a",
    trackPrice: Double = 1.29,
    currency: String = "USD",
    durationMs: Long = 240_000L,
) = TrackDto(
    id = id, title = title, artist = artist, album = album,
    artworkUrl = artworkUrl, previewUrl = previewUrl,
    trackPrice = trackPrice, currency = currency, durationMs = durationMs,
)
