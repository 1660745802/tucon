package com.example.tucon.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "http://konghonglee.top:8000/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface SettingsApiService {
    @GET("version")
    suspend fun getSettings(): SettingsInfo
}

object SettingsApi {
    val retrofitService: SettingsApiService by lazy {
        retrofit.create(SettingsApiService::class.java)
    }
}