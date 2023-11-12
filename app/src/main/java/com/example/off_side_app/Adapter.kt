package com.example.off_side_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.off_side_app.databinding.RecyclerviewItemBinding

class Adapter : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    // 구장 정보를 저장하는 data class Ground의 정보를 가지는 배열
    var groundItems = ArrayList<Ground>()

    // 생성된 뷰 홀더에 값 지정
    class MyViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(groundItem: Ground) {
            // 뷰 홀더의 제목과 설명
            binding.nameText.text = groundItem.name
            binding.descriptionText.text = groundItem.address

            // 뷰 홀더 클릭시 그라운드 뷰로
            binding.cardView.setOnClickListener {
                val intent: Intent = Intent(it.context, DetailViewActivity::class.java)
                intent.putExtra("currentName", groundItem.name)
                intent.putExtra("currentDes", groundItem.address)
                intent.putExtra("currentImagePath", groundItem.imagePath)
                it.context.startActivity(intent)
            }
        }
    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(groundItems[position])
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return groundItems.size
    }
}