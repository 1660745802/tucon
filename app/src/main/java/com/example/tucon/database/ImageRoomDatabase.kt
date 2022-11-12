package com.example.tucon.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImageFavorited::class], version = 1, exportSchema = false)
abstract class ImageRoomDatabase : RoomDatabase() {
    abstract fun imageFavoritedDao(): ImageFavoritedDao

    companion object {
        @Volatile
        private var INSTANCE: ImageRoomDatabase? = null

        fun getDatabase(context: Context): ImageRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageRoomDatabase::class.java,
                    "image_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

}