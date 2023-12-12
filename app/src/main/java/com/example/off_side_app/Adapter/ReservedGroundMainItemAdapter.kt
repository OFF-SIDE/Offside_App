package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.R
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.ReservedGroundInfo
import com.example.off_side_app.databinding.GroundRecyclerviewItemBinding
import com.example.off_side_app.databinding.ReservedGroundRecyclerviewItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ReservedGroundMainItemAdapter(val items: List<ReservedGroundInfo>) : RecyclerView.Adapter<ReservedGroundMainItemAdapter.ReservedGroundItemViewHolder>() {

    inner class ReservedGroundItemViewHolder(private val binding: ReservedGroundRecyclerviewItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(ground: ReservedGroundInfo){
            binding.nameText.text = ground.groundInfo.name
            binding.descriptionText.text = ground.groundInfo.address
            Glide.with(binding.imageView)
                .load(ground.groundInfo.image)
                .error(R.drawable.baseline_error_24)
                .into(binding.imageView)

            binding.reservationTimeText.text = GetMergedTime(ground.date, ground.time)
        }
        fun GetMergedTime(date: String, time: String): String{
            val inputFormat = SimpleDateFormat("yyMMdd HHmm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yy/MM/dd HH:mm", Locale.getDefault())

            try {
                val combinedDateTime = "$date $time"
                val parsedDateTime = inputFormat.parse(combinedDateTime)

                // 변환된 날짜와 시간을 원하는 형식으로 출력
                return outputFormat.format(parsedDateTime!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservedGroundItemViewHolder {
        return ReservedGroundItemViewHolder(ReservedGroundRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ReservedGroundItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}