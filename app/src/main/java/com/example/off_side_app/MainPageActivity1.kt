package com.example.off_side_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        adapter.groundItems.add(Ground("서울월드컵경기장", "대한민국 어딘가", "이미지패스"))
        adapter.groundItems.add(Ground("서울월드컵경기장1", "대한민국 어딘가1", "이미지패스1"))
        adapter.groundItems.add(Ground("서울월드컵경기장2", "대한민국 어딘가2", "이미지패스2"))
        adapter.groundItems.add(Ground("서울월드컵경기장3", "대한민국 어딘가3", "이미지패스3"))
        adapter.groundItems.add(Ground("서울월드컵경기장4", "대한민국 어딘가4", "이미지패스4"))
        adapter.groundItems.add(Ground("서울월드컵경기장5", "대한민국 어딘가5", "이미지패스5"))
        adapter.groundItems.add(Ground("서울월드컵경기장6", "대한민국 어딘가6", "이미지패스6"))

        // 어댑터 연결
        binding.recyclerView.adapter = adapter
    }
}