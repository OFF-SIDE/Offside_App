package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.data.NotificationInfo
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch

class NotificationViewModel: ViewModel() {
    private val repository = Repository()

    private val _result = MutableLiveData<List<NotificationInfo>>()
    val result: LiveData<List<NotificationInfo>>
        get() = _result

    fun getNotificationData(contactPhone: String) = viewModelScope.launch {
        //Log.d("GroundMainViewModel", repository.getAllData().toString())
        try{
            _result.value = repository.getNotificationData(contactPhone)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}