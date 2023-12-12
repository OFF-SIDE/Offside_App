package com.example.off_side_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.data.GroundInfoForPost
import com.example.off_side_app.data.GroundInfoWithAvailableTime
import com.example.off_side_app.data.ImageUrl
import com.example.off_side_app.data.RefereeInfoForPost
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class RefereeViewModel: ViewModel() {
    private val repository = Repository()

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    private val _detail = MutableLiveData<RefereeInfoWithAvailableTime>()
    val detail: LiveData<RefereeInfoWithAvailableTime>
        get() = _detail

    fun postRefereeData(refereeInfoForPost: RefereeInfoForPost) = viewModelScope.launch {
        repository.postRefereeData(refereeInfoForPost)
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

    fun getRefereeDetailData(id: Int, date: Int) = viewModelScope.launch {
        try{
            val referee: RefereeInfoWithAvailableTime = repository.getRefereeDetail(id)
            _detail.value = referee
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}