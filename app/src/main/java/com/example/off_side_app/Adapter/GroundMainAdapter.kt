package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.R
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.Ground
import com.example.off_side_app.data.Header
import com.example.off_side_app.databinding.RecyclerviewHeaderBinding
import com.example.off_side_app.databinding.RecyclerviewItemBinding


class GroundMainAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    interface OnItemClickListener {
        fun onItemClick(groundItem: Ground, position: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        var groundItems = AppDataManager.getOriginalGroundItems()
        return if (groundItems[position].locationPosition == groundItems.getOrNull(position - 1)?.locationPosition) {
            TYPE_ITEM
        } else {
            TYPE_HEADER
        }
    }

    class HeaderViewHolder(val binding: RecyclerviewHeaderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(headerItem: Header){
            binding.locationHeaderText.setText(headerItem.locationPosition)
        }
    }

    // 생성된 뷰 홀더에 값 지정
    class GroundItemViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groundItem: Ground) {
            // 뷰 홀더의 제목과 설명
            binding.nameText.text = groundItem.name
            binding.descriptionText.text = groundItem.address
            // binding.imageView.setImageURI(groundItem.imagePath)

            Glide.with(binding.imageView)
                .load(groundItem.imagePath)
                .error(R.drawable.baseline_error_24)
                .into(binding.imageView)
        }
    }

    // 뷰 홀더에 표시될 레이아웃 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundItemViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GroundItemViewHolder(binding)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            // 고쳐야함
            holder.bind()
        }
        else if (holder is GroundItemViewHolder){
            var groundItems = AppDataManager.getOriginalGroundItems()
            holder.bind(groundItems[position])
            holder.itemView.setOnClickListener {
                listener?.onItemClick(groundItems[position], position)
            }
        }
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        var groundItems = AppDataManager.getOriginalGroundItems()
        return groundItems.size
    }
}