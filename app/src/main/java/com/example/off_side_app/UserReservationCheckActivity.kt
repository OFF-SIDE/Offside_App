package com.example.off_side_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.off_side_app.Adapter.ReservedGroundMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.data.ReservedGroundInfo
import com.example.off_side_app.data.ReservedGroundInfoGroup
import com.example.off_side_app.databinding.ActivityUserReservationCheckBinding
import com.example.off_side_app.ui.UserReservationCheckViewModel

class UserReservationCheckActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserReservationCheckBinding.inflate(layoutInflater)
    }

    private lateinit var reservedGroundMainAdapter: ReservedGroundMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var userPhone = ""
        val viewModel = ViewModelProvider(this)[UserReservationCheckViewModel::class.java]

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        // 어댑터 할당
        reservedGroundMainAdapter = ReservedGroundMainAdapter()
        binding.recyclerView.adapter = reservedGroundMainAdapter

        // api 호출하여 뷰모델의 result 업데이트
        viewModel.getReservedGroundData(userPhone)

        // Q. onCreate 안에 observe가 있어도 이벤트가 전달되나?
        viewModel.result.observe(this){ notice ->
            val convertedgroup = DivideGroup(notice)
            reservedGroundMainAdapter.setList(convertedgroup)
            reservedGroundMainAdapter.notifyDataSetChanged()
        }

        var swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.getReservedGroundData(userPhone)
            val convertedgroup = DivideGroup(viewModel.result.value!!)
            reservedGroundMainAdapter.setList(convertedgroup)
            reservedGroundMainAdapter.notifyDataSetChanged()
            swipe.isRefreshing = false
        }
    }

    fun DivideGroup(items: List<ReservedGroundInfo>): MutableList<ReservedGroundInfoGroup>{
        val groupedItems = mutableListOf<ReservedGroundInfoGroup>()
        for (location in AppDataManager.nearLocations){
            val groupedItem = ReservedGroundInfoGroup(
                location,
                mutableListOf()
            )
            for (item in items){
                if(item.groundInfo.location == location){
                    groupedItem.groupedReservedGround.add(item)
                }
            }
            if(!groupedItem.groupedReservedGround.isEmpty())
                groupedItems.add(groupedItem)
        }
        return groupedItems
    }
}