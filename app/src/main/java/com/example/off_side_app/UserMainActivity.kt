package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.databinding.ActivityUserMainBinding
import com.example.off_side_app.ui.GroundMainViewModel


class UserMainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserMainBinding.inflate(layoutInflater)
    }
    private lateinit var groundMainAdapter: GroundMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var contactPhone = ""
        val location = ""
        val viewModel = ViewModelProvider(this)[GroundMainViewModel::class.java]
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        // 어댑터 할당
        groundMainAdapter = GroundMainAdapter{ stadiumId, externalUrl: String->
            val intent: Intent
            if(externalUrl == "")
                intent = Intent(this@UserMainActivity, ReservationActivity::class.java)
            else {
                intent = Intent(this@UserMainActivity, ExternalReservationActivity::class.java)
                intent.putExtra("externalUrl", externalUrl)
            }

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

        binding.refereeButton.setOnClickListener{
            val intent = Intent(this@UserMainActivity, RefereeMainActivity::class.java)
            startActivity(intent)
        }

        binding.reservedGroundBtn.setOnClickListener {
            val intent = Intent(this@UserMainActivity, UserReservationCheckActivity::class.java)
            startActivity(intent)
        }

        binding.notificationButton.setOnClickListener {
            val intent = Intent(this@UserMainActivity, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    fun DivideGroup(items: List<GroundInfo>): MutableList<GroundInfoGroup>{
        val groupedItems = mutableListOf<GroundInfoGroup>()
        for (location in AppDataManager.nearLocations){
            val groupedItem = GroundInfoGroup(
                location,
                mutableListOf()
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