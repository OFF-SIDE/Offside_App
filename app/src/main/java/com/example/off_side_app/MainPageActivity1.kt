package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.databinding.ActivityMainPage1Binding

class MainPageActivity1 : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainPage1Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        val adapter = Adapter()
        var groundItems = AppDataManager.getOriginalGroundItems()

        // 어댑터 연결
        binding.recyclerView.adapter = adapter

        binding.checkListButton.setOnClickListener {
            var groundItems = AppDataManager.getOriginalGroundItems()
            val listAsString = groundItems.joinToString("\n")
            Toast.makeText(this, listAsString, Toast.LENGTH_SHORT).show()
        }

        val groundActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // 다른 액티비티에서 결과를 받았을 때의 처리
                    val data: Intent? = result.data
                    adapter.notifyDataSetChanged()
                }
            }

        binding.addGroundButton.setOnClickListener{
            val intent = Intent(this, GroundActivity::class.java)
            groundActivityResultLauncher.launch(intent)
        }
    }
}