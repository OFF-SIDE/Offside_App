package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageButton2.setOnClickListener{
            val intent = Intent(this, GroundActivity::class.java)
            startActivity(intent)
        }

        binding.mainGroundButton.setOnClickListener{
            val intent = Intent(this, MainPageActivity1::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}