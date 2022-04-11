package com.example.tucon.network

import com.squareup.moshi.Json

data class MyPicture(
    val code: String,
    @Json(name = "imgurl") val imgSrcUrl: String,
    val width: String,
    val height: String
)

//data class MyPicture(
//    val error: String,
//    val data: List<PictureIntent>
//) {
//    val imgSrcUrl: String get() = data[0].urls.original.replace("https://i.pixiv.cat/", "https://api.pixiv.moe/image/i.pximg.net/")
//}
//
//data class PictureIntent(
//    val pid: Int,
//    val p: Int,
//    val uid: Int,
//    val title: String,
//    val author: String,
//    val r18: Boolean,
//    val width: Int,
//    val height: Int,
//    val tags: List<String>,
//    val ext: String,
//    val uploadDate: String,
//    val urls: PictureUrl
//)
//data class PictureUrl(
//    val original: String,
////    val regular: String,
////    val small: String,
////    val thumb: String,
////    val mini: String
//)
