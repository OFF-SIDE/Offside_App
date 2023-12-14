package com.example.off_side_app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.off_side_app.databinding.ActivityMainBinding
import com.example.off_side_app.databinding.ActivityRefereeBinding
import com.example.off_side_app.ui.GroundViewModel
import com.example.off_side_app.ui.LoadingDialog
import com.example.off_side_app.ui.RefereeViewModel

class RefereeActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityRefereeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[RefereeViewModel::class.java]

        val currentId = intent.getIntExtra("id", -1)

        if(currentId != -1){
            // 기존 구장의 경우 기존 정보로 텍스트 채우기
            viewModel.getRefereeDetailData(currentId)  // 오늘 날짜로
            val dialog = LoadingDialog(this@RefereeActivity)

            dialog.show()

            viewModel.detail.observe(this) { notice ->
                val refereeInfo = notice
                try {
                    // 1. 이미지
                    Glide.with(binding.pictureImageView)
                        .load(refereeInfo.image)
                        .circleCrop()
                        .error(R.drawable.baseline_error_24)
                        .into(binding.pictureImageView)

                    // 2. 이름

                    binding.userNameText.setText(refereeInfo.name)
                    var phone = StringBuilder(refereeInfo.contactPhone)
                    phone.insert(3, "-").insert(8, "-")
                    binding.userPhoneText.setText(phone.toString())
                    binding.userCommentText.setText(refereeInfo.comment)
                    var price = refereeInfo.price!!.toString()
                    price = "$price 원/h"
                    binding.userPriceText.setText(price)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
                dialog.dismiss()
            }
        }
    }
}