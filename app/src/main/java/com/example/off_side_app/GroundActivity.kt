package com.example.off_side_app

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.off_side_app.databinding.ActivityGroundBinding
import java.text.SimpleDateFormat

class GroundActivity : AppCompatActivity() {

    val PERM_STORAGE = 9

    val REQ_GALLERY = 10

    val binding by lazy{ ActivityGroundBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    fun setViews(){
        binding.selectImageBtn.setOnClickListener{
            openGallery()
        }
    }

    var realUri: Uri? = null

    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    fun createImageUri(filename:String, mimeType:String) : Uri?{
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

    }

    fun newfileName() : String{
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    fun loadBitmap(photoUri: Uri): Bitmap? {
        try{
            return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
                val source = ImageDecoder.createSource(contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            }
        } catch(e:Exception){
            e.printStackTrace()
        }
        return null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
           when(requestCode){
                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        binding.pictureImageView.setImageURI(uri)
                }
            }    
        }
        }
    }
}


