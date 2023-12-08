package com.example.off_side_app.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.bumptech.glide.Glide
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.FileOutputStream


object ImageUtil {
    fun getImageMultipartBody(context: Context, imageUrl: Uri?): MultipartBody.Part? {
        try {
            /*
            val file: File = Glide.with(context)
                .asFile()
                .load(imageUrl)
                .submit()
                .get()

             */
            //val file = File(absolutelyPath(imageUrl, context))
            val file = getFileFromUri(imageUrl!!, context)
            // 파일을 MultipartBody.Part로 변환
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file!!)
            //val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            return MultipartBody.Part.createFormData("file", file?.name, requestFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    // 절대경로 변환
    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }
    fun getFileFromUri(uri: Uri, context: Context): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                if (columnIndex != -1) {
                    val filePath = it.getString(columnIndex)
                    return File(filePath)
                }
            }
        }
        return null
    }
}