package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.off_side_app.data.GroundInfoGroup
import com.example.off_side_app.databinding.GroundRecyclerviewHeaderBinding
class GroundMainAdapter(val onClick: (Int?, String)->(Unit)) : RecyclerView.Adapter<GroundMainAdapter.GroundViewHolder>() {
    private var items = listOf<GroundInfoGroup>()

    inner class GroundViewHolder(private val binding: GroundRecyclerviewHeaderBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(group: GroundInfoGroup){
            binding.locationHeaderText.setText(group.location)
            binding.rvGroupedGround.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = GroundMainItemAdapter(onClick, group.groupedGround)
                addItemDecoration(
                    DividerItemDecoration(binding.root.context,
                        DividerItemDecoration.VERTICAL)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroundMainAdapter.GroundViewHolder {
        return GroundViewHolder(GroundRecyclerviewHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: GroundMainAdapter.GroundViewHolder, position: Int) {
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