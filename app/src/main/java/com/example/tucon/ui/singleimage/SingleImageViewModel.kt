package com.example.tucon.ui.singleimage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SingleImageViewModel: ViewModel() {
    private val _imgUrl = MutableLiveData<String>("")
    val imgUrl: LiveData<String> = _imgUrl

    fun setUrl(uri: String) {
        _imgUrl.value = uri
    }
}