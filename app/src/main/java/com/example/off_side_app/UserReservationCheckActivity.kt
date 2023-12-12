package com.example.off_side_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.tooling.data.EmptyGroup.location
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.Adapter.RefereeMainAdapter
import com.example.off_side_app.Adapter.ReservedGroundMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.data.ReservedGroundInfo
import com.example.off_side_app.data.ReservedGroundInfoGroup
import com.example.off_side_app.databinding.ActivityGroundMainBinding
import com.example.off_side_app.databinding.ActivityUserReservationCheckBinding
import com.example.off_side_app.ui.GroundMainViewModel
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
        reservedGroundMainAdapter = ReservedGroundMainAdapter{ stadiumId->
            // 클릭 메소드 필요한가?
            val intent = Intent(this@UserReservationCheckActivity, GroundActivity::class.java)
            intent.putExtra("stadiumId", stadiumId)
            startActivity(intent)
        }
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

        /*
        // 디버깅용 버튼
        binding.checkListButton.setOnClickListener {
            var groundItems = AppDataManager.getOriginalGroundItems()
            val listAsString = groundItems.joinToString("\n")
            Toast.makeText(this, listAsString, Toast.LENGTH_SHORT).show()
        }
         */

        // 구장 추가 버튼
        binding.addGroundButton.setOnClickListener{
            val intent = Intent(this, GroundRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun DivideGroup(items: List<ReservedGroundInfo>): MutableList<ReservedGroundInfoGroup>{
        val groupedItems = mutableListOf<ReservedGroundInfoGroup>()
        for (location in AppDataManager.nearLocations){
            val groupedItem = GroundInfoGroup(
                location,
                mutableListOf<GroundInfo>()
            )
            for (item in items){
                if(item.location == location){
                    groupedItem.groupedGround.add(item)
                }
            }
            if(!groupedItem.groupedGround.isEmpty())
                groupedItems.add(groupedItem)
        }
        return groupedItems
    }
}