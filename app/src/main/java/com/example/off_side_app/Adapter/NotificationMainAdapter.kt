package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.off_side_app.R
import com.example.off_side_app.data.GroundInfo
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.data.NotificationInfo
import com.example.off_side_app.databinding.GroundRecyclerviewHeaderBinding
import com.example.off_side_app.databinding.GroundRecyclerviewItemBinding
import com.example.off_side_app.databinding.NotificationRecyclerviewItemBinding

class NotificationMainAdapter() : RecyclerView.Adapter<NotificationMainAdapter.NotificationViewHolder>() {
    private var items = listOf<NotificationInfo>()

    inner class NotificationViewHolder(private val binding: NotificationRecyclerviewItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(noti: NotificationInfo){
            binding.titleText.setText(noti.title)
            binding.messageText.setText(noti.message)
            binding.timeView.setText(noti.createdTime.toString().take(10))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationMainAdapter.NotificationViewHolder {
        return NotificationViewHolder(NotificationRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NotificationMainAdapter.NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //
    fun setList(notification: List<NotificationInfo>) {
        items = notification
    }
}