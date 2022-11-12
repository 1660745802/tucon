package com.example.tucon.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tucon.network.SettingsApi
import kotlinx.coroutines.launch

class AppSettingsViewModel: ViewModel() {

    var currentVersion = ""
    private var _latestVersion = MutableLiveData<String>("")
    val latestVersion: LiveData<String> = _latestVersion
    private val _cacheSize = MutableLiveData<String>()
    val cacheSize: LiveData<String> = _cacheSize

    fun updateLatestVersion() {
        viewModelScope.launch {
            val settingInfo = SettingsApi.retrofitService.getSettings()
            _latestVersion.value = settingInfo.version
        }
    }
    fun setCacheSize(s: String) {
        _cacheSize.value = s
    }
}