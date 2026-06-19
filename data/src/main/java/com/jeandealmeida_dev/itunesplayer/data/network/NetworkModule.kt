package com.jeandealmeida_dev.itunesplayer.data.network

import com.jeandealmeida_dev.itunesplayer.data.remote.ItunesService
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date

object NetworkModule {

    val moshi: Moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Volatile
    var itunesService: ItunesService = createService(ItunesService.BASE_URL)

    fun createService(baseUrl: String): ItunesService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ItunesService::class.java)
}
