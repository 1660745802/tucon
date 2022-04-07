package com.example.tucon.viewmodel

import androidx.lifecycle.*
import com.example.tucon.database.ImageFavorited
import com.example.tucon.database.ImageFavoritedDao
import com.example.tucon.network.MyPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class FavoriteImageViewModel(private val imageFavoritedDao: ImageFavoritedDao) : ViewModel() {


    private val _photos : LiveData<List<ImageFavorited>> = imageFavoritedDao.getFavoriteImageList().asLiveData()
    val photos : LiveData<List<ImageFavorited>> = _photos

    private fun insertImage(imageFavorited: ImageFavorited) {
        viewModelScope.launch {
            if (withContext(Dispatchers.IO) {
                imageFavoritedDao.isFavorited(imageFavorited.imgSrcUrl).isEmpty()
            }) imageFavoritedDao.insert(imageFavorited)
        }
    }
    private fun deleteImage(imgUrl: String) {
        viewModelScope.launch {
            imageFavoritedDao.delete(imgUrl)
        }
    }

    fun addNewImage(imgUrl: String) : Boolean {
        insertImage(ImageFavorited(imgSrcUrl =  imgUrl))
        return true
    }
    fun dislikeImage(imgUrl: String) {
        deleteImage(imgUrl)
    }

}
class FavoriteImageViewModelFactory(private val imageFavoritedDao: ImageFavoritedDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteImageViewModel(imageFavoritedDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}