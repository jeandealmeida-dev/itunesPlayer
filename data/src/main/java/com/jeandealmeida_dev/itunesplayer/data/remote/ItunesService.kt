package com.jeandealmeida_dev.itunesplayer.data.remote

import com.jeandealmeida_dev.itunesplayer.data.remote.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20,
    ): SearchDto

    @GET("lookup")
    suspend fun lookup(
        @Query("id") id: Long,
        @Query("entity") entity: String = "song",
    ): SearchDto

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }
}
