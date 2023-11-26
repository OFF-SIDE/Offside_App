package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.off_side_app.Adapter.GroundMainAdapter
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.Ground
import com.example.off_side_app.databinding.ActivityGroundMainBinding


class GroundMainActivity : AppCompatActivity(), GroundMainAdapter.OnItemClickListener {
    private val binding by lazy {
        ActivityGroundMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 아이템을 가로로 하나씩 보여줌
        binding.recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        val adapter = GroundMainAdapter()

        // 어댑터 연결
        binding.recyclerView.adapter = adapter

        val groundActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // GroundActivity에서 돌아왔을 때
                    val data: Intent? = result.data
                    adapter.notifyDataSetChanged()
                }
            }

        adapter.setOnItemClickListener(object: GroundMainAdapter.OnItemClickListener{
            override fun onItemClick(groundItem: Ground, position: Int) {
                val intent = Intent(this@GroundMainActivity, GroundActivity::class.java)
                intent.putExtra("currentName", groundItem.name)
                intent.putExtra("currentDes", groundItem.address)
                intent.putExtra("currentImagePath", groundItem.imagePath)
                intent.putExtra("currentDataListIdx", position)
                intent.putExtra("currentLocationPosition", groundItem.locationPosition)
                groundActivityResultLauncher.launch(intent)
            }
        })

        // 디버깅용 버튼
        binding.checkListButton.setOnClickListener {
            var groundItems = AppDataManager.getOriginalGroundItems()
            val listAsString = groundItems.joinToString("\n")
            Toast.makeText(this, listAsString, Toast.LENGTH_SHORT).show()
        }

        // 구장 추가 버튼
        binding.addGroundButton.setOnClickListener{
            val intent = Intent(this, GroundActivity::class.java)
            groundActivityResultLauncher.launch(intent)
        }
    }

    override fun onItemClick(groundItem: Ground, position: Int){
        val intent = Intent(this, GroundActivity::class.java)
        intent.putExtra("currentName", groundItem.name)
        intent.putExtra("currentDes", groundItem.address)
        intent.putExtra("currentImagePath", groundItem.imagePath)
        intent.putExtra("currentListIdx", position)
        startActivity(intent)
    }
}