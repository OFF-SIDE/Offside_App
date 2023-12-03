package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.GroundInfo
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch

class GroundMainViewModel: ViewModel() {
    private val repository = Repository()

    private val _result = MutableLiveData<List<GroundInfo>>()
    val result: LiveData<List<GroundInfo>>
        get() = _result

    fun getAllData(contactPhone: String, location: String) = viewModelScope.launch {
        //Log.d("GroundMainViewModel", repository.getAllData().toString())
        _result.value = repository.getAllData(contactPhone, location)
    }
}