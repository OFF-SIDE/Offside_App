package com.example.off_side_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.off_side_app.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserBinding
    var currentURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = Adapter()


    }

        private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){

                if(it.resultCode == RESULT_OK && it.data != null){

                    var uri = it.data!!.data

                    Glide.with(this)
                        .load(uri)
                        .into(binding.pictureImageView)

                    currentURI = uri

                }
        }
}






