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

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ItunesService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val itunesService: ItunesService = retrofit.create(ItunesService::class.java)
}
