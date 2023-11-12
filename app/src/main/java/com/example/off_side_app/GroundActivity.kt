package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.off_side_app.databinding.ActivityGroundBinding

class GroundActivity : AppCompatActivity() {

    lateinit var binding: ActivityGroundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)

        }
    }

        private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){

                if(it.resultCode == RESULT_OK && it.data != null){

                    val uri = it.data!!.data

                    Glide.with(this)
                        .load(uri)
                        .into(binding.pictureImageView)

                }
        }
}






