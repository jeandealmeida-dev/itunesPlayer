package com.jeandealmeida_dev.itunesplayer.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.SearchDto
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.TrackDto
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TrackPagingSourceTest {

    private val service: ItunesService = mockk()
    private val pagingSource = TrackPagingSource(service, "rock")

    @Test
    fun `GIVEN first page request WHEN load is called THEN prevKey is null and nextKey equals load size`() = runTest {
        coEvery { service.search(any(), any(), any(), any()) } returns SearchDto(listOf(aTrackDto()), 1)

        val params = PagingSource.LoadParams.Refresh<Int>(key = null, loadSize = 20, placeholdersEnabled = false)
        val result = pagingSource.load(params) as PagingSource.LoadResult.Page

        assertNull(result.prevKey)
        assertEquals(20, result.nextKey)
    }

    @Test
    fun `GIVEN page with offset 20 WHEN load is called THEN prevKey is 0 and nextKey is 40`() = runTest {
        coEvery { service.search(any(), any(), any(), any()) } returns SearchDto(listOf(aTrackDto(id = 21L)), 1)

        val params = PagingSource.LoadParams.Append(key = 20, loadSize = 20, placeholdersEnabled = false)
        val result = pagingSource.load(params) as PagingSource.LoadResult.Page

        assertEquals(0, result.prevKey)
        assertEquals(40, result.nextKey)
    }

    @Test
    fun `GIVEN service returns empty results WHEN load is called THEN nextKey is null`() = runTest {
        coEvery { service.search(any(), any(), any(), any()) } returns SearchDto(emptyList(), 0)

        val params = PagingSource.LoadParams.Append(key = 20, loadSize = 20, placeholdersEnabled = false)
        val result = pagingSource.load(params) as PagingSource.LoadResult.Page

        assertNull(result.nextKey)
    }

    @Test
    fun `GIVEN service throws WHEN load is called THEN returns a LoadResult Error`() = runTest {
        coEvery { service.search(any(), any(), any(), any()) } throws RuntimeException("network failure")

        val params = PagingSource.LoadParams.Refresh<Int>(key = null, loadSize = 20, placeholdersEnabled = false)
        val result = pagingSource.load(params)

        assertTrue(result is PagingSource.LoadResult.Error)
    }

    @Test
    fun `GIVEN service returns tracks WHEN load is called THEN DTOs are mapped to domain models`() = runTest {
        val dto = aTrackDto(id = 7L, title = "Roxanne")
        coEvery { service.search(any(), any(), any(), any()) } returns SearchDto(listOf(dto), 1)

        val params = PagingSource.LoadParams.Refresh<Int>(key = null, loadSize = 20, placeholdersEnabled = false)
        val result = pagingSource.load(params) as PagingSource.LoadResult.Page

        assertEquals(1, result.data.size)
        assertEquals(7L, result.data[0].id)
        assertEquals("Roxanne", result.data[0].title)
    }

    @Test
    fun `GIVEN a search term WHEN load is called THEN the term and offset are forwarded to the service`() = runTest {
        coEvery { service.search(any(), any(), any(), any()) } returns SearchDto(emptyList(), 0)

        val params = PagingSource.LoadParams.Append(key = 20, loadSize = 20, placeholdersEnabled = false)
        pagingSource.load(params)

        coVerify { service.search(term = "rock", offset = 20, limit = 20, media = any()) }
    }

    @Test
    fun `GIVEN null anchor position WHEN getRefreshKey is called THEN returns null`() {
        val state: PagingState<Int, Track> = mockk()
        every { state.anchorPosition } returns null

        val key = pagingSource.getRefreshKey(state)

        assertNull(key)
    }

    @Test
    fun `GIVEN anchor position whose closest page has prevKey WHEN getRefreshKey is called THEN returns prevKey plus page size`() {
        val page: PagingSource.LoadResult.Page<Int, Track> = mockk()
        every { page.prevKey } returns 20

        val state: PagingState<Int, Track> = mockk()
        every { state.anchorPosition } returns 25
        every { state.closestPageToPosition(25) } returns page

        val key = pagingSource.getRefreshKey(state)

        assertEquals(40, key) // prevKey(20) + PAGE_SIZE(20)
    }

    @Test
    fun `GIVEN anchor position whose closest page has only nextKey WHEN getRefreshKey is called THEN returns nextKey minus page size`() {
        val page: PagingSource.LoadResult.Page<Int, Track> = mockk()
        every { page.prevKey } returns null
        every { page.nextKey } returns 40

        val state: PagingState<Int, Track> = mockk()
        every { state.anchorPosition } returns 5
        every { state.closestPageToPosition(5) } returns page

        val key = pagingSource.getRefreshKey(state)

        assertEquals(20, key) // nextKey(40) - PAGE_SIZE(20)
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
    collectionId: Long = 4213L,
) = TrackDto(
    id = id, title = title, artist = artist, album = album,
    artworkUrl = artworkUrl, previewUrl = previewUrl,
    trackPrice = trackPrice, currency = currency, durationMs = durationMs,
    collectionId = collectionId,
)
