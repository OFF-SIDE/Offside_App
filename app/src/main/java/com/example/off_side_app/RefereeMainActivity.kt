package com.example.off_side_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.off_side_app.Adapter.RefereeMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.RefereeInfo
import com.example.off_side_app.data.RefereeInfoGroup
import com.example.off_side_app.databinding.ActivityRefreeMainBinding
import com.example.off_side_app.ui.RefereeMainViewModel

class RefereeMainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRefreeMainBinding.inflate(layoutInflater)
    }
    private lateinit var refereeMainAdapter: RefereeMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val date = ""
        val viewModel = ViewModelProvider(this)[RefereeMainViewModel::class.java]

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        // 어댑터 할당
        refereeMainAdapter = RefereeMainAdapter{ id->
            val intent = Intent(this@RefereeMainActivity, RefereeActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
        binding.recyclerView.adapter = refereeMainAdapter

        // api 호출하여 뷰모델의 result 업데이트
        viewModel.getRefereeData(date)

        // Q. onCreate 안에 observe가 있어도 이벤트가 전달되나?
        viewModel.result.observe(this){ notice ->
            val convertedgroup = DivideGroup(notice)
            refereeMainAdapter.setList(convertedgroup)
            refereeMainAdapter.notifyDataSetChanged()
        }

        var swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.getRefereeData(date)
            val convertedgroup = DivideGroup(viewModel.result.value!!)
            refereeMainAdapter.setList(convertedgroup)
            refereeMainAdapter.notifyDataSetChanged()
            swipe.isRefreshing = false
        }


        // 구장 추가 버튼
        binding.addRefereeButton.setOnClickListener{
            val intent = Intent(this, RefereeRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun DivideGroup(items: List<RefereeInfo>): MutableList<RefereeInfoGroup>{
        val groupedItems = mutableListOf<RefereeInfoGroup>()
        for (location in AppDataManager.nearLocations){
            val groupedItem = RefereeInfoGroup(
                location,
                mutableListOf()
            )
            for (item in items){
                if(item.location == location){
                    groupedItem.groupedReferee.add(item)
                }
            }
            if(!groupedItem.groupedReferee.isEmpty())
                groupedItems.add(groupedItem)
        }
        return groupedItems
    }
}