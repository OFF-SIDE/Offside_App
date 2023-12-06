package com.example.off_side_app.data

import android.content.Context
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object ImageUtil {
    fun loadAndConvertImage(context: Context, imageUrl: String): MultipartBody.Part? {
        try {
            val file: File = Glide.with(context)
                .asFile()
                .load(imageUrl)
                .submit()
                .get()

            // 파일을 MultipartBody.Part로 변환
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            return MultipartBody.Part.createFormData("file", file.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}