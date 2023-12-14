package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.data.GroundInfoForPost
import com.example.off_side_app.data.GroundInfoWithAvailableTime
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import com.example.off_side_app.data.ImageUrl

class GroundViewModel: ViewModel() {
    private val repository = Repository()

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    private val _detail = MutableLiveData<GroundInfoWithAvailableTime>()
    val detail: LiveData<GroundInfoWithAvailableTime>
        get() = _detail

    fun postGroundData(groundInfoForPost: GroundInfoForPost) = viewModelScope.launch {
        repository.postGroundData(groundInfoForPost)
    }

    fun uploadImageData(image: MultipartBody.Part) = viewModelScope.launch {
        try{
            val imageUrl: ImageUrl = repository.uploadImageData(image)
            _image.value = imageUrl.fileUrl
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getGroundDetailData(stadiumId: Int, date: Int?) = viewModelScope.launch {
        try{
            val ground: GroundInfoWithAvailableTime = repository.getGroundDetail(stadiumId, date)
            _detail.value = ground
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}