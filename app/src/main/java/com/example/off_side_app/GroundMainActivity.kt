package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.Ground
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

        val contactPhone = ""
        val location = "마포구"
        val viewModel = ViewModelProvider(this)[GroundMainViewModel::class.java]

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        // 어댑터 할당
        groundMainAdapter = GroundMainAdapter{ stadiumId->
            val intent = Intent(this@GroundMainActivity, GroundActivity::class.java)
            intent.putExtra("stadiumId", stadiumId)
            startActivity(intent)
        }
        binding.recyclerView.adapter = groundMainAdapter

        // api 호출하여 뷰모델의 result 업데이트
        viewModel.getAllData(contactPhone, location)

        /*
        // Q. onCreate 안에 observe가 있어도 이벤트가 전달되나?
        viewModel.result.observe(this, Observer {
            val customAdapter = GroundMainAdapter(this, it)
            binding.recyclerView.adapter = customAdapter
        })
         */

        // Q. onCreate 안에 observe가 있어도 이벤트가 전달되나?
        viewModel.result.observe(this, Observer{ notice ->
            groundMainAdapter.setList(notice)
            groundMainAdapter.notifyDataSetChanged()
        })


        /*
        val adapter = GroundMainAdapter()

        // 어댑터 연결
        binding.recyclerView.adapter = adapter

         */
        /*
        val groundActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // GroundActivity에서 돌아왔을 때
                    val data: Intent? = result.data
                    adapter.notifyDataSetChanged()
                }
            }

         */

        /*
        adapter.setOnItemClickListener(object: GroundMainAdapter.OnItemClickListener{
            override fun onItemClick(groundItem: Ground, position: Int) {
                val intent = Intent(this@GroundMainActivity, GroundActivity::class.java)
                intent.putExtra("stadiumId", "")
                //groundActivityResultLauncher.launch(intent)
                startActivity(intent)
            }
        })
         */


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
            val intent = Intent(this, GroundActivity::class.java)
            startActivity(intent)
        }
    }

    /*
    override fun onItemClick(groundItem: Ground, position: Int){
        val intent = Intent(this, GroundActivity::class.java)
        intent.putExtra("currentName", groundItem.name)
        intent.putExtra("currentDes", groundItem.address)
        intent.putExtra("currentImagePath", groundItem.imagePath)
        intent.putExtra("currentListIdx", position)
        startActivity(intent)
    }

     */
}