package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.GroundInfo
import com.example.off_side_app.GroundInfoForPost
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch

class GroundViewModel: ViewModel() {
    private val repository = Repository()

    private val _result = MutableLiveData<List<GroundInfo>>()
    val result: LiveData<List<GroundInfo>>
        get() = _result

    fun postGroundData(groundInfoForPost: GroundInfoForPost) = viewModelScope.launch {
        repository.postGroundData(groundInfoForPost)
    }
}