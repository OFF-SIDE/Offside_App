package com.example.off_side_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.off_side_app.Adapter.NotificationMainAdapter
import com.example.off_side_app.databinding.ActivityNotificationBinding
import com.example.off_side_app.ui.NotificationViewModel

class NotificationActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }
    private lateinit var notificationMainAdapter: NotificationMainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var contactPhone = "01028994421"
        val viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        // 어댑터 할당
        notificationMainAdapter = NotificationMainAdapter()
        binding.recyclerView.adapter = notificationMainAdapter

        // api 호출하여 뷰모델의 result 업데이트
        viewModel.getNotificationData(contactPhone)

        viewModel.result.observe(this){ notice ->
            notificationMainAdapter.setList(notice)
            notificationMainAdapter.notifyDataSetChanged()
        }

        var swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.getNotificationData(contactPhone)
            notificationMainAdapter.setList(viewModel.result.value!!)
            notificationMainAdapter.notifyDataSetChanged()
            swipe.isRefreshing = false
        }

    }
}