package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.R
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.databinding.RecyclerviewItemBinding

/*
class UserMainItemAdapter(val onClick: (Int?)->(Unit), val items: List<GroundInfo>) : RecyclerView.Adapter<GroundMainItemAdapter.GroundItemViewHolder>() {
    /*
    interface OnItemClickListener {
        fun onItemClick(groundItem: Ground, position: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemViewType(position: Int): Int {
        var groundItems = AppDataManager.getOriginalGroundItems()
        return if (groundItems[position].getType() == TYPE_GROUND) {
            TYPE_GROUND
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_GROUND) {
            val binding =
                RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GroundItemViewHolder(binding)
        }
        else{
            val binding =
                RecyclerviewHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HeaderViewHolder(binding)
        }
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var groundItems = AppDataManager.getOriginalGroundItems()
        if(holder is HeaderViewHolder){
            // 고쳐야함
            holder.bind(groundItems[position] as Header)
        }
        else if (holder is GroundItemViewHolder){
            holder.bind(groundItems[position] as Ground)
            holder.itemView.setOnClickListener {
                listener?.onItemClick(groundItems[position] as Ground, position)
            }
        }
    }
     */
    inner class GroundItemViewHolder(private val binding: RecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(ground: GroundInfo){
            binding.nameText.text = ground.name
            binding.descriptionText.text = ground.address
            Glide.with(binding.imageView)
                .load(ground.image)
                .error(R.drawable.baseline_error_24)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                // 리사이클러뷰 아이템에 클릭이벤트 발생
                onClick(ground.stadiumId) // 생성자 파라미터로 받은 람다함수 onClick 실행
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundItemViewHolder {
        return GroundItemViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: GroundItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

 */