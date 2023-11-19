package com.example.off_side_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityGroundBinding

class GroundActivity : AppCompatActivity() {

    lateinit var binding: ActivityGroundBinding
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentName = intent.getStringExtra("currentName")
        val currentDes = intent.getStringExtra("currentDes")
        val currentImagePath: Uri? = intent.getParcelableExtra("currentImagePath")
        val currentListIdx = intent.getIntExtra("currentDataListIdx", -1)
        var newFlag = false

        if(currentName!=null)
            binding.nameText.setText(currentName)
        else
            newFlag = true

        if(currentDes!=null)
            binding.addressText.setText(currentDes)

        if(currentImagePath!=null) {
            uri = currentImagePath
            Glide.with(this)
                .load(uri)
                .error(R.drawable.baseline_error_24)
                .into(binding.pictureImageView)
        }


        binding.selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        binding.groudSaveButton.setOnClickListener {
            var adapter = Adapter()

            val name = binding.nameText.text.toString()
            val address = binding.addressText.text.toString()

            var groundItems = AppDataManager.getOriginalGroundItems()

            // 이미지 선택 여부 확인
            if (uri != null && name != null && address != null) {
                if(newFlag)
                    groundItems.add(Ground(name, address, uri))
                else{
                    groundItems[currentListIdx].name = name
                    groundItems[currentListIdx].address = address
                    groundItems[currentListIdx].imagePath = uri
                }
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                // 이미지가 선택되지 않은 경우
                Toast.makeText(this, "내용을 모두 작성하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            // 갤러리에서 선택한 이미지의 Uri를 저장
            uri = result.data!!.data
            Glide.with(this)
                .load(uri)
                .into(binding.pictureImageView)
        }
    }
/*
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

            if(it.resultCode == RESULT_OK && it.data != null){

                uri = it.data!!.data

                Glide.with(this)
                    .load(uri)
                    .into(binding.pictureImageView)
            }

    }
 */
}






