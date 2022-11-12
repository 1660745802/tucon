package com.example.tucon.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageFavoritedDao {

    @Query("select * from image_favorited")
    fun getFavoriteImageList(): Flow<List<ImageFavorited>>

    @Query("select * from image_favorited where imgurl = :imgUrl")
    fun isFavorited(imgUrl: String): List<ImageFavorited>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(imageFavorited: ImageFavorited)

    @Query("delete from image_favorited where imgurl = :imgUrl ")
    suspend fun delete(imgUrl: String)

}