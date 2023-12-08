package com.example.off_side_app.ui

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.off_side_app.GroundInfo
import com.example.off_side_app.GroundInfoForPost
import com.example.off_side_app.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import android.app.Application

class GroundViewModel: ViewModel() {
    private val repository = Repository()

    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image

    fun postGroundData(groundInfoForPost: GroundInfoForPost) = viewModelScope.launch {
        repository.postGroundData(groundInfoForPost)
    }

    fun uploadImageData(image: MultipartBody.Part) = viewModelScope.launch {
        try{
            val imageUrl:String = repository.uploadImageData(image)
            _image.value = imageUrl
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /*
    fun uploadImageData(image: MultipartBody.Part) = viewModelScope.launch {
        // 외부 저장소 읽기 권한이 있는지 확인
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(
                getCurrentActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            // 권한이 이미 허용된 경우 이미지 업로드 수행
            try {
                val imageUrl: String = repository.uploadImageData(image)
                _image.value = imageUrl
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 권한 요청 결과를 받는 코드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // 권한 요청 결과 확인
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용된 경우 이미지 업로드 수행
                    try {
                        val imageUrl: String = repository.uploadImageData(image)
                        _image.value = imageUrl
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    // 권한이 거부된 경우 사용자에게 메시지 표시 등의 처리 수행
                    // ...
                }
            }
            // 다른 권한 요청 코드에 대한 처리 추가 가능
        }
    }

     */
}