package com.example.tucon.viewmodel

import android.util.Log
import android.widget.Toast
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

    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch {
            try {
                var photoList : MutableList<Deferred<MyPicture>> = mutableListOf()
                for (i in 1..20) photoList.add(async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() })
                _photos.value = photoList.awaitAll()

            } catch (e: Exception) {
                _status.value = e.message
            }
        }
    }
    fun getPhotosAgain() {
        getPhotos()
    }

}