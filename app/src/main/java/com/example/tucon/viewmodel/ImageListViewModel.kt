package com.example.tucon.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tucon.network.MyPicture
import com.example.tucon.network.PictureApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
                val photo1 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo2 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo3 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo4 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo5 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo6 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo7 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo8 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo9 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo10 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo11 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo12 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo13 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo14 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo15 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo16 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo17 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo18 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo19 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                val photo20 = async(Dispatchers.IO) { PictureApi.retrofitService.getPhotos() }
                _photos.value = listOf(
                    photo1.await(),
                    photo2.await(),
                    photo3.await(),
                    photo4.await(),
                    photo5.await(),
                    photo6.await(),
                    photo7.await(),
                    photo8.await(),
                    photo9.await(),
                    photo10.await(),
                    photo11.await(),
                    photo12.await(),
                    photo13.await(),
                    photo14.await(),
                    photo15.await(),
                    photo16.await(),
                    photo17.await(),
                    photo18.await(),
                    photo19.await(),
                    photo20.await()
                )
            } catch (e: Exception) {
                _status.value = e.message
            }
        }
    }
    fun getPhotosAgain() {
        getPhotos()
    }

}