package com.example.tucon.database

import android.app.Application

class FavoriteImageApplication : Application() {
    val database: ImageRoomDatabase by lazy { ImageRoomDatabase.getDatabase(this) }
}