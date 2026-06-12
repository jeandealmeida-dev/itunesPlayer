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
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
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

    @Test
    fun `GIVEN a search term WHEN search returns an empty list THEN returns empty list`() = runTest {
        coEvery { service.search(any()) } returns SearchDto(emptyList(), 0)

        val result = repository.search("jazz")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN service throws WHEN search is called THEN exception propagates to caller`() = runTest {
        coEvery { service.search(any()) } throws RuntimeException("network error")

        try {
            repository.search("rock")
            fail("Expected RuntimeException to propagate")
        } catch (e: RuntimeException) {
            assertEquals("network error", e.message)
        }
    }

    @Test
    fun `GIVEN a collection id WHEN getAlbumTracks is called THEN all tracks with non-null ids are returned`() = runTest {
        val dtos = listOf(aTrackDto(id = 1L), aTrackDto(id = 2L), aTrackDto(id = 3L))
        coEvery { service.lookup(100L) } returns SearchDto(dtos, 3)

        val result = repository.getAlbumTracks(100L)

        assertEquals(3, result.size)
        coVerify(exactly = 1) { service.lookup(100L) }
    }

    @Test
    fun `GIVEN lookup returns a dto with null id WHEN getAlbumTracks is called THEN that entry is filtered out`() = runTest {
        // iTunes lookup returns the album record itself first — it has no trackId
        val dtos = listOf(aTrackDto(id = null), aTrackDto(id = 1L), aTrackDto(id = 2L))
        coEvery { service.lookup(any()) } returns SearchDto(dtos, 3)

        val result = repository.getAlbumTracks(100L)

        assertEquals(2, result.size)
        assertEquals(listOf(1L, 2L), result.map { it.id })
    }

    @Test
    fun `GIVEN lookup returns only null-id dtos WHEN getAlbumTracks is called THEN returns empty list`() = runTest {
        coEvery { service.lookup(any()) } returns SearchDto(listOf(aTrackDto(id = null)), 1)

        val result = repository.getAlbumTracks(100L)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN a collection id WHEN getAlbumTracks is called THEN delegates the id unmodified to service`() = runTest {
        val collectionId = 55L
        coEvery { service.lookup(collectionId) } returns SearchDto(emptyList(), 0)

        repository.getAlbumTracks(collectionId)

        coVerify { service.lookup(collectionId) }
    }

    @Test
    fun `GIVEN a search term WHEN searchPaged is called THEN returns a non-null flow`() {
        val flow = repository.searchPaged("electronic")

        assertNotNull(flow)
    }
}

private fun aTrackDto(
    id: Long? = 1L,
    title: String = "Track Title",
    artist: String = "Artist",
    album: String = "Album",
    artworkUrl: String = "https://example.com/art.jpg",
    previewUrl: String = "https://example.com/preview.m4a",
    trackPrice: Double = 1.29,
    currency: String = "USD",
    durationMs: Long = 240_000L,
    collectionId: Long = 4213L,
) = TrackDto(
    id = id, title = title, artist = artist, album = album,
    artworkUrl = artworkUrl, previewUrl = previewUrl,
    trackPrice = trackPrice, currency = currency, durationMs = durationMs,
    collectionId = collectionId
)
