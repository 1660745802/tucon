package com.example.tucon.network

import com.squareup.moshi.Json

data class MyPicture(
    val id: String,
    @Json(name = "imgurl") val imgSrcUrl: String
)

