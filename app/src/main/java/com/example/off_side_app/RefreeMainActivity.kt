package com.example.off_side_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.databinding.ActivityGroundMainBinding
import com.example.off_side_app.databinding.ActivityRefreeMainBinding

class RefreeMainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRefreeMainBinding.inflate(layoutInflater)
    }
    private lateinit var refereeMainAdapter: GroundMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refree_main)
    }
}