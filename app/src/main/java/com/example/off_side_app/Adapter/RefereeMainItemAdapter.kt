package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.R
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.RefereeInfo
import com.example.off_side_app.databinding.RefereeRecyclerviewItemBinding

class RefereeMainItemAdapter(val onClick: (Int?)->(Unit), val items: List<RefereeInfo>) : RecyclerView.Adapter<RefereeMainItemAdapter.RefereeItemViewHolder>() {

    inner class RefereeItemViewHolder(private val binding: RefereeRecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(referee: RefereeInfo){
            binding.nameText.text = referee.name
            binding.descriptionText.text = referee.location
            Glide.with(binding.imageView)
                .load(referee.image)
                .error(R.drawable.baseline_error_24)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                // 리사이클러뷰 아이템에 클릭이벤트 발생
                onClick(referee.id) // 생성자 파라미터로 받은 람다함수 onClick 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefereeItemViewHolder {
        return RefereeItemViewHolder(RefereeRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RefereeItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}