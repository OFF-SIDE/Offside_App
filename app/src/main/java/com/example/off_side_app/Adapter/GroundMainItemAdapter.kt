package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.R
import com.example.off_side_app.databinding.GroundRecyclerviewItemBinding

class GroundMainItemAdapter(val onClick: (Int?, String)->(Unit), val items: List<GroundInfo>) : RecyclerView.Adapter<GroundMainItemAdapter.GroundItemViewHolder>() {

    inner class GroundItemViewHolder(private val binding: GroundRecyclerviewItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(ground: GroundInfo){
            binding.nameText.text = ground.name
            binding.descriptionText.text = ground.address
            Glide.with(binding.imageView)
                .load(ground.image)
                .error(R.drawable.baseline_error_24)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                // 리사이클러뷰 아이템에 클릭이벤트 발생
                onClick(ground.stadiumId, ground.externalUrl) // 생성자 파라미터로 받은 람다함수 onClick 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundItemViewHolder {
        return GroundItemViewHolder(GroundRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: GroundItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}