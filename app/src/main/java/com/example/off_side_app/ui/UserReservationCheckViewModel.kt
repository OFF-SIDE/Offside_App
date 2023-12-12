package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.data.ReservedGroundInfo
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch

class UserReservationCheckViewModel: ViewModel() {
    private val repository = Repository()

    private val _result = MutableLiveData<List<ReservedGroundInfo>>()
    val result: LiveData<List<ReservedGroundInfo>>
        get() = _result

    fun getReservedGroundData(userPhone: String) = viewModelScope.launch {
        try{
            _result.value = repository.getReservedGroundData(userPhone)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}