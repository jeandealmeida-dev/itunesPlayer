package com.jeandealmeida_dev.itunesplayer.domain.usecase

import androidx.paging.PagingData
import com.jeandealmeida_dev.itunesplayer.domain.model.Track
import com.jeandealmeida_dev.itunesplayer.domain.repository.TrackRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertSame
import org.junit.Test

class GetPagedTracksUseCaseTest {

    private val repository: TrackRepository = mockk()
    private val useCase = GetPagedTracksUseCase(repository)

    @Test
    fun `GIVEN a search term WHEN invoke is called THEN returns the flow from repository`() {
        val expected: Flow<PagingData<Track>> = flowOf(PagingData.empty())
        every { repository.searchPaged("rock") } returns expected

        val result = useCase("rock")

        assertSame(expected, result)
    }

    @Test
    fun `GIVEN a search term WHEN invoke is called THEN delegates term unmodified to repository`() {
        val term = "Taylor Swift"
        every { repository.searchPaged(term) } returns flowOf(PagingData.empty())

        useCase(term)

        verify(exactly = 1) { repository.searchPaged(term) }
    }
}
