package com.jeandealmeida_dev.itunesplayer.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class TrackDtoTest {

    @Test
    fun `GIVEN fully populated dto WHEN toDomain is called THEN all fields are mapped correctly`() {
        val dto = TrackDto(
            id = 1L, title = "Yesterday", artist = "The Beatles",
            album = "Help!", artworkUrl = "https://art.jpg",
            previewUrl = "https://preview.m4a", trackPrice = 1.29,
            currency = "USD", durationMs = 185_000L, collectionId = 4213L
        )

        val track = dto.toDomain()

        assertEquals(1L, track.id)
        assertEquals("Yesterday", track.title)
        assertEquals("The Beatles", track.artist)
        assertEquals("Help!", track.album)
        assertEquals("https://art.jpg", track.artworkUrl)
        assertEquals("https://preview.m4a", track.previewUrl)
        assertEquals(1.29, track.trackPrice, 0.001)
        assertEquals("USD", track.currency)
        assertEquals(185_000L, track.durationMs)
        assertEquals(4213L, track.collectionId)
    }

    @Test
    fun `GIVEN dto with all null fields WHEN toDomain is called THEN fallback values are used`() {
        val dto = TrackDto(null, null, null, null, null, null, null, null, null, null)

        val track = dto.toDomain()

        assertEquals(-1L, track.id)
        assertEquals("", track.title)
        assertEquals("", track.artist)
        assertEquals("", track.album)
        assertEquals("", track.artworkUrl)
        assertEquals("", track.previewUrl)
        assertEquals(0.0, track.trackPrice, 0.001)
        assertEquals("", track.currency)
        assertEquals(0L, track.durationMs)
        assertEquals(0L, track.collectionId)
    }

    @Test
    fun `GIVEN dto with null price and duration WHEN toDomain is called THEN numeric fields default to zero`() {
        val dto = TrackDto(
            id = 5L, title = "Test", artist = "Artist",
            album = null, artworkUrl = null, previewUrl = null,
            trackPrice = null, currency = null, durationMs = null, collectionId = null,
        )

        val track = dto.toDomain()

        assertEquals(0.0, track.trackPrice, 0.001)
        assertEquals(0L, track.durationMs)
    }
}
