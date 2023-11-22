package com.example.off_side_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.data.AppDataManager
import com.example.off_side_app.data.Ground
import com.example.off_side_app.databinding.RecyclerviewItemBinding


interface OnItemClickListener {
    fun onItemClick(groundItem: Ground, position: Int)
}
class Adapter() : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(groundItem: Ground, position: Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    // 생성된 뷰 홀더에 값 지정
    class MyViewHolder(val binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(groundItem: Ground, position: Int) {
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



    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var groundItems = AppDataManager.getOriginalGroundItems()
        holder.bind(groundItems[position], position)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(groundItems[position], position)
        }
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        var groundItems = AppDataManager.getOriginalGroundItems()
        return groundItems.size
    }
}