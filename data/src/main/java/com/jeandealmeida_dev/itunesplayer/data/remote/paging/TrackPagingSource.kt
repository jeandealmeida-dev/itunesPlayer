package com.jeandealmeida_dev.itunesplayer.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import com.jeandealmeida_dev.itunesplayer.data.remote.dto.toDomain
import com.jeandealmeida_dev.itunesplayer.domain.model.Track

class TrackPagingSource(
    private val service: ItunesService,
    private val term: String,
) : PagingSource<Int, Track>() {

    override fun getRefreshKey(state: PagingState<Int, Track>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(PAGE_SIZE)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        val offset = params.key ?: 0
        return try {
            val results = service.search(term = term, offset = offset, limit = params.loadSize).results
            val tracks = results.map { it.toDomain() }
            LoadResult.Page(
                data = tracks,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (tracks.isEmpty()) null else offset + params.loadSize,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
