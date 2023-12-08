package com.example.off_side_app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityUserPhoneNumberBinding

class UserPhoneNumberActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityUserPhoneNumberBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            val number = binding.phoneNumberText.text.toString()
            if(number.isEmpty()){
                Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val sharedPreference = getSharedPreferences("ground", MODE_PRIVATE)
                val editor  : SharedPreferences.Editor = sharedPreference.edit()
                editor.putString("phoneNumber", number)

                editor.commit() // data 저장!

                AppDataManager.phoneNumber = number

                val intent = Intent(this, MainActivity::class.java)
                setResult(RESULT_OK, intent)

                finish()
            }
        }
    }
}