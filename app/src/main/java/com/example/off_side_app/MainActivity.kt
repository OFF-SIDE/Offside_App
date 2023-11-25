package com.example.off_side_app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityMainBinding
import java.math.BigInteger.TWO

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        // SubOne에서 결과를 받아옴
        if(it.resultCode == RESULT_OK){
            val intent = Intent(this, MainPageActivity1::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageButton2.setOnClickListener{
            val intent = Intent(this, GroundActivity::class.java)
            startActivity(intent)
        }

        binding.mainGroundButton.setOnClickListener{
            val sharedPreference = getSharedPreferences("ground", MODE_PRIVATE)
            val savedPhoneNumber = sharedPreference.getString("phoneNumber", "no number")

            if (savedPhoneNumber == "no number"){
                // 만약 sharedPreference에 전화번호가 없다면 입력창으로
                val intent = Intent(this, UserPhoneNumberActivity::class.java)
                activityResultLauncher.launch(intent)
            }
            else{
                val intent = Intent(this, MainPageActivity1::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}