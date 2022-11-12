package com.example.tucon.network

import com.squareup.moshi.Json

data class SettingsInfo (
    @Json(name = "latest_version") val version: String
)