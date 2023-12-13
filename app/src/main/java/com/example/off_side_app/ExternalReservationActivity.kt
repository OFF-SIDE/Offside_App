package com.example.off_side_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.off_side_app.databinding.ActivityExternalReservationBinding
import com.example.off_side_app.databinding.ActivityGroundBinding
import com.example.off_side_app.ui.GroundViewModel
import com.example.off_side_app.ui.LoadingDialog

class ExternalReservationActivity : AppCompatActivity() {
    lateinit var binding: ActivityExternalReservationBinding
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExternalReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[GroundViewModel::class.java]

        val currentStadiumId = intent.getIntExtra("stadiumId", -1)

        if(currentStadiumId != -1){
            // 기존 구장의 경우 기존 정보로 텍스트 채우기
            viewModel.getGroundDetailData(currentStadiumId, 1210)  // 오늘 날짜로
            val dialog = LoadingDialog(this@ExternalReservationActivity)

            dialog.show()

            viewModel.detail.observe(this) { notice ->
                val groundInfo = notice
                try {
                    // 1. 이미지
                    Glide.with(binding.pictureImageView)
                        .load(groundInfo.image)
                        .error(R.drawable.baseline_error_24)
                        .into(binding.pictureImageView)

                    // 2. 이름
                    binding.userNameText.setText(groundInfo.name)
                    binding.userPhoneText.setText(groundInfo.contactPhone)
                    binding.userAddressText.setText(groundInfo.address)
                    binding.userCommentText.setText(groundInfo.comment)
                    val price = groundInfo.price!!.toString()
                    if(price == "0")
                        binding.userPriceText.setText("가격 문의")
                    else
                        binding.userPriceText.setText(price)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                dialog.dismiss()
            }
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.visitExternalPage.setOnClickListener {
            val url = "https://cyber.sogang.ac.kr/ilos/main/main_form.acl"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

    }

    fun checkContentsFull(binding: ActivityGroundBinding, uri: Uri?): Boolean{
        if(binding.userAddressText.text.toString() == "")
            return false
        if(binding.userNameText.text.toString() == "")
            return false
        if(binding.userCommentText.text.toString() == "")
            return false
        if(binding.userPhoneText.text.toString() == "")
            return false
        if(binding.userPriceText.text.toString() == "")
            return false
        return true
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
}