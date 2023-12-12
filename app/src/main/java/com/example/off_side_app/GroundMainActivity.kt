package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.Adapter.GroundMainItemAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.databinding.ActivityGroundMainBinding
import com.example.off_side_app.ui.GroundMainViewModel

class GroundMainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityGroundMainBinding.inflate(layoutInflater)
    }
    private lateinit var groundMainAdapter: GroundMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var contactPhone = ""
        //contactPhone = AppDataManager.phoneNumber!!
        val location = ""
        val viewModel = ViewModelProvider(this)[GroundMainViewModel::class.java]

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        // 어댑터 할당
        groundMainAdapter = GroundMainAdapter{ stadiumId, externalUrl->
            // 구장의 경우 외부 구장에 대해서 알 필요가 있나?
            val intent = Intent(this@GroundMainActivity, GroundActivity::class.java)
            intent.putExtra("stadiumId", stadiumId)
            startActivity(intent)
        }
        binding.recyclerView.adapter = groundMainAdapter

        // api 호출하여 뷰모델의 result 업데이트
        viewModel.getGroundData(contactPhone, location)

        // Q. onCreate 안에 observe가 있어도 이벤트가 전달되나?
        viewModel.result.observe(this){ notice ->
            val convertedgroup = DivideGroup(notice)
            groundMainAdapter.setList(convertedgroup)
            groundMainAdapter.notifyDataSetChanged()
        }

        var swipe = findViewById<SwipeRefreshLayout>(R.id.swipe)
        swipe.setOnRefreshListener {
            viewModel.getGroundData(contactPhone, location)
            val convertedgroup = DivideGroup(viewModel.result.value!!)
            groundMainAdapter.setList(convertedgroup)
            groundMainAdapter.notifyDataSetChanged()
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

    fun DivideGroup(items: List<GroundInfo>): MutableList<GroundInfoGroup>{
        val groupedItems = mutableListOf<GroundInfoGroup>()
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