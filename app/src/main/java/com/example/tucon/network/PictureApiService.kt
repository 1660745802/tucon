package com.example.tucon.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://imgapi.cn/"
//    "https://api.lolicon.app/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PictureApiService {
    @GET("api.php?zd=zsy&fl=fengjing&gs=json")
//    @GET("setu/v2")
    suspend fun getPhotos(): MyPicture
}

object PictureApi {
    val retrofitService: PictureApiService by lazy {
        retrofit.create(PictureApiService::class.java)
    }
}