package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.data.RefereeInfo
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch

class RefereeMainViewModel: ViewModel() {
    private val repository = Repository()

    private val _result = MutableLiveData<List<RefereeInfo>>()
    val result: LiveData<List<RefereeInfo>>
        get() = _result

    fun getRefereeData(location: String, date: String) = viewModelScope.launch {
        //Log.d("GroundMainViewModel", repository.getAllData().toString())
        try{
            _result.value = repository.getRefereeData(location, date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}