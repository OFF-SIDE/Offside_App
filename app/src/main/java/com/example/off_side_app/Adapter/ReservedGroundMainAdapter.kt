package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.databinding.GroundRecyclerviewHeaderBinding
import com.example.off_side_app.databinding.ReservedGroundRecyclerviewHeaderBinding

// 받는 데이터 형식에 따라 리스트 타입 변경
class ReservedGroundMainAdapter(val onClick: (Int?)->(Unit)) : RecyclerView.Adapter<ReservedGroundMainAdapter.ReservedGroundViewHolder>() {
    private var items = listOf<GroundInfoGroup>()

    inner class ReservedGroundViewHolder(private val binding: ReservedGroundRecyclerviewHeaderBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(group: GroundInfoGroup){
            binding.locationHeaderText.setText(group.location)
            binding.rvGroupedReservedGround.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = ReservedGroundMainItemAdapter(onClick, group.groupedGround)
                addItemDecoration(
                    DividerItemDecoration(binding.root.context,
                        DividerItemDecoration.VERTICAL)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservedGroundMainAdapter.ReservedGroundViewHolder {
        return ReservedGroundViewHolder(ReservedGroundRecyclerviewHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ReservedGroundMainAdapter.ReservedGroundViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //
    fun setList(ground: List<GroundInfoGroup>) {
        items = ground
    }
}