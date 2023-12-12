package com.example.off_side_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.off_side_app.databinding.ActivityGroundMainBinding
import com.example.off_side_app.databinding.ActivityRefereeRegisterBinding

class RefereeRegisterActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRefereeRegisterBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referee_register)
    }
}