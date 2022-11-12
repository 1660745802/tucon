package com.example.tucon.utils

import android.net.Uri
import android.util.Log
import retrofit2.http.Path

object ImageUrlUtil {
    fun getUrlName(imgUrl: String?): String {
        return Uri.parse(imgUrl).pathSegments.last()
    }
    fun getUrlId(imgUrl: String?): String {
        val name = getUrlName(imgUrl)
        return name.substring(0, name.length - 4)
    }
    fun changeUrlQuality(imgUrl: String?, quality: Int): String {
        val uri = Uri.parse(imgUrl)
        val set = uri.queryParameterNames
        var newUrl: String = getBaseUrl(imgUrl) + "?quality=$quality"
        for (s in set) {
            if (s == "quality") {
                continue
            } else {
                if (newUrl.last() != '&') {
                    newUrl += "&"
                }
                newUrl += s + "=" + uri.getQueryParameter(s)
            }
        }
        return newUrl
    }
    fun getBaseUrl(imgUrl: String?): String {
        val uri = Uri.parse(imgUrl)
        return uri.scheme!! + "://" + uri.authority!! + uri.path!!
    }
}