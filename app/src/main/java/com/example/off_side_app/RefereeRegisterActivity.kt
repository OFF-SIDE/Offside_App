package com.example.off_side_app

import android.R
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.GroundInfoForPost
import com.example.off_side_app.data.ImageUtil
import com.example.off_side_app.data.RefereeInfoForPost
import com.example.off_side_app.databinding.ActivityGroundMainBinding
import com.example.off_side_app.databinding.ActivityGroundRegisterBinding
import com.example.off_side_app.databinding.ActivityRefereeRegisterBinding
import com.example.off_side_app.ui.GroundViewModel
import com.example.off_side_app.ui.LoadingDialog
import com.example.off_side_app.ui.RefereeViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RefereeRegisterActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRefereeRegisterBinding.inflate(layoutInflater)
    }
    private var uri: Uri? = null
    private val buttonBrightnessMap = mutableMapOf<Int, Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.selectImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        val viewModel = ViewModelProvider(this)[RefereeViewModel::class.java]

        // 스피너
        val itemArray = AppDataManager.nearLocations
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, itemArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        // 신규 생성의 경우 location의 position=0
        var currentPosition = 0

        binding.spinner.setSelection(currentPosition)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                currentPosition = position
                // 선택한 구에 대한 작업 수행
                Toast.makeText(this@RefereeRegisterActivity, currentPosition.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 동작
                Toast.makeText(this@RefereeRegisterActivity, "아무 것도 선택되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.saveBtn.setOnClickListener {
            // 신규 생성의 경우
            if(checkContentsFull(binding, uri)){
                // 이미지 업로드
                //val multipartBody = ImageUtil.getImageMultipartBody(this, uri)
                val filePath = ImageUtil.absolutelyPath(uri, this)
                val file = File(filePath)

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

                viewModel.uploadImageData(multipartBody)
                val dialog = LoadingDialog(this@RefereeRegisterActivity)

                dialog.show()

                var serverUrl:String = ""
                viewModel.image.observe(this) { notice ->
                    serverUrl = notice
                    try {
                        // 서버에 데이터 post
                        val body = getBodyForPost(binding, currentPosition, serverUrl!!)
                        viewModel.postRefereeData(body)
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                    dialog.dismiss()
                    finish()
                }
            }

        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        for (i in 10..22) {
            val buttonId = resources.getIdentifier("hour${i}_btn", "id", packageName)
            val button = findViewById<Button>(buttonId)

            // 초기 밝기 상태를 false로 설정합니다.
            buttonBrightnessMap[buttonId] = false

            button.setOnClickListener {
                if(buttonBrightnessMap[button.id]!!){
                    button.setBackgroundResource(com.example.off_side_app.R.drawable.buttonshape)
                    buttonBrightnessMap[button.id] = false
                }
                else{
                    button.setBackgroundResource(com.example.off_side_app.R.drawable.buttonshape2)
                    buttonBrightnessMap[button.id] = true
                }
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

    fun checkContentsFull(binding: ActivityGroundRegisterBinding, uri: Uri?): Boolean{
        if(binding.addressText.text.toString() == "")
            return false
        if(binding.nameText.text.toString() == "")
            return false
        if(binding.commentText.text.toString() == "")
            return false
        if(binding.contactPhoneText.text.toString() == "")
            return false
        if(binding.priceText.text.toString() == "")
            return false
        return true
    }

    fun getBodyForPost(binding: ActivityRefereeRegisterBinding, currentPosition: Int, serverUrl: String): RefereeInfoForPost {
        var refereeInfoForPost = RefereeInfoForPost(
            AppDataManager.nearLocations[currentPosition],
            binding.nameText.text.toString(),
            binding.contactPhoneText.text.toString(),
            binding.addressText.text.toString(),
            binding.commentText.text.toString(),
            binding.priceText.text.toString().toInt(),
            serverUrl
        )
        return refreeInfoForPost
    }
}