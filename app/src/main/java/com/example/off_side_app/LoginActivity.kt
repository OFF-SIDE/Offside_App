package com.example.off_side_app

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.example.off_side_app.databinding.ActivityLoginBinding
import com.example.off_side_app.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.phoneNumberSaveBtn.setOnClickListener {
            sharedPreferences = getPreferences(MODE_PRIVATE)

            with(sharedPreferences.edit()){
                var testString = binding.phoneNumberTextView.text.toString()
                putString("userPhoneNumber", binding.phoneNumberTextView.text.toString())

                var testString1 = sharedPreferences.getString("userPhoneNumber", "11")
                apply()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}