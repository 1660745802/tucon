package com.example.tucon.ui.imagelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tucon.network.MyPicture
import com.example.tucon.network.PictureApi
import kotlinx.coroutines.*
import java.lang.Exception

class ImageListViewModel: ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _photos = MutableLiveData<List<MyPicture>>()
    val photos: LiveData<List<MyPicture>> = _photos

    var searchMode: Int = 0
    fun changeSearchMode() {
        searchMode = (searchMode + 1) % 3
    }

    init {
        _photos.value = listOf()
        getPhotos()
    }
    private fun delete(l: List<MyPicture>, j: Int) : List<MyPicture> {
        val ans = mutableListOf<MyPicture>()
        for (i in j until l.size) {
            ans.add(l[i])
        }
        return ans
    }
    private fun merge(l1: List<MyPicture>, l2: List<MyPicture>) : List<MyPicture> {
        val ans = mutableListOf<MyPicture>()
        for (i in l1) ans.add(i)
        for (i in l2) ans.add(i)
        return ans
    }
    private fun getPhotos(i: Int = 5) {
        viewModelScope.launch {
            try {
                var photoList : MutableList<Deferred<MyPicture>> = mutableListOf()
                for (i in 1..i) {
                    if (searchMode == 0) {
                        photoList.add(async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() })
                    } else if (searchMode == 2) {
                        photoList.add(async(Dispatchers.IO) { PictureApi.retrofitService.getPhotosR() })
                    } else {
                        val flag = ((Math.random() * 100).toInt() % 4) == 0
                        if (flag) {
                            photoList.add(async(Dispatchers.IO) { PictureApi.retrofitService.getPhotosR() })
                        } else {
                            photoList.add(async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() })
                        }
                    }
                }
                _photos.value = merge(_photos.value!!, photoList.awaitAll())
            } catch (e: Exception) {
                _status.value = e.message
            }
        }
    }
    fun getPhotosAgain(i: Int = 5, flag: Boolean = true) {
        if (flag) {
            _photos.value = listOf()
        }
        getPhotos(i)
    }
    fun deletefore(i: Int) {
        if (_photos.value!!.size < 5) {
            return
        }
        viewModelScope.launch {
            _photos.value = delete(_photos.value!!,
                0.coerceAtLeast((i - 2).coerceAtMost(_photos.value!!.size - 5))
            )
        }
    }

}