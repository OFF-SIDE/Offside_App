package com.example.off_side_app

import android.app.DatePickerDialog
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
import java.util.Calendar

class RefereeMainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRefreeMainBinding.inflate(layoutInflater)
    }
    private lateinit var refereeMainAdapter: RefereeMainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var date = ""
        val viewModel = ViewModelProvider(this)[RefereeMainViewModel::class.java]

        val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            binding.dateText.text = "${year}/${month}/${day}"
        }

        binding.dateText.setOnClickListener {

            val cal = Calendar.getInstance()
            val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                binding.dateText.text = "${year}/${month+1}/${day}"
                date = "${(year % 100).toString().takeLast(2)}${String.format("%02d", month + 1)}${String.format("%02d", day)}"
                viewModel.getRefereeData(date)
                val convertedgroup = DivideGroup(viewModel.result.value!!)
                refereeMainAdapter.setList(convertedgroup)
                refereeMainAdapter.notifyDataSetChanged()
            }
            DatePickerDialog(this, data, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }


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