package com.jeandealmeida_dev.itunesplayer

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class FakeTrackRepository(
    private val tracks: List<Track> = emptyList(),
    private val track: Track? = null,
) : TrackRepository {
    override suspend fun search(term: String): List<Track> = tracks
    override suspend fun getTrack(id: Long): Track? = track
    override suspend fun getAlbumTracks(collectionId: Long): List<Track> = tracks
    override fun searchPaged(term: String): Flow<PagingData<Track>> = Pager(
        config = PagingConfig(pageSize = tracks.size.coerceAtLeast(1)),
        pagingSourceFactory = {
            object : PagingSource<Int, Track>() {
                override fun getRefreshKey(state: PagingState<Int, Track>): Int? = null
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> =
                    LoadResult.Page(data = tracks, prevKey = null, nextKey = null)
            }
        },
    ).flow
}

fun aTrack(
    id: Long = 1L,
    title: String = "Yesterday",
    artist: String = "The Beatles",
    album: String = "Help!",
    artworkUrl: String = "",
    previewUrl: String = "https://example.com/preview.m4a",
    trackPrice: Double = 1.29,
    currency: String = "USD",
    durationMs: Long = 185_000L,
    collectionId: Long = 0L,
) = Track(
    id = id, title = title, artist = artist, album = album,
    artworkUrl = artworkUrl, previewUrl = previewUrl,
    trackPrice = trackPrice, currency = currency, durationMs = durationMs,
    collectionId = collectionId,
)
