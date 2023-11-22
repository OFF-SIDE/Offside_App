package com.example.off_side_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.Ground
import com.example.off_side_app.databinding.ActivityMainPage1Binding

class MainPageActivity1 : AppCompatActivity(), Adapter.OnItemClickListener {
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
        var groundItems = AppDataManager.getOriginalGroundItems()

        // 어댑터 연결
        binding.recyclerView.adapter = adapter

        val groundActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // 다른 액티비티에서 결과를 받았을 때의 처리
                    val data: Intent? = result.data
                    adapter.notifyDataSetChanged()
                }
            }

        adapter.setOnItemClickListener(object: Adapter.OnItemClickListener{
            override fun onItemClick(groundItem: Ground, position: Int) {
                val intent = Intent(this@MainPageActivity1, GroundActivity::class.java)
                intent.putExtra("currentName", groundItem.name)
                intent.putExtra("currentDes", groundItem.address)
                intent.putExtra("currentImagePath", groundItem.imagePath)
                intent.putExtra("currentDataListIdx", position)
                intent.putExtra("currentLocationPosition", groundItem.locationPosition)
                groundActivityResultLauncher.launch(intent)
                /*
                Toast.makeText(binding.recyclerView.context,
                    "${groundItem.name}\n${groundItem.address}\n${groundItem.imagePath.toString()}\n${position}",
                    Toast.LENGTH_SHORT).show()
                */
            }
        })

        binding.checkListButton.setOnClickListener {
            var groundItems = AppDataManager.getOriginalGroundItems()
            val listAsString = groundItems.joinToString("\n")
            Toast.makeText(this, listAsString, Toast.LENGTH_SHORT).show()
        }



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