package com.example.off_side_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.off_side_app.databinding.ActivityMainBinding
import com.example.off_side_app.databinding.ActivityRefereeBinding

class RefereeActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityRefereeBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referee)
    }
}