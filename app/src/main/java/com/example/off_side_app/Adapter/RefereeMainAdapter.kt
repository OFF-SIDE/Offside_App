package com.example.off_side_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.off_side_app.data.RefereeInfoGroup
import com.example.off_side_app.databinding.RefereeRecyclerviewHeaderBinding

class RefereeMainAdapter(val onClick: (Int?)->(Unit)): RecyclerView.Adapter<RefereeMainAdapter.RefereeViewHolder>() {
    private var items = listOf<RefereeInfoGroup>()

    inner class RefereeViewHolder(private val binding: RefereeRecyclerviewHeaderBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(group: RefereeInfoGroup){
            binding.locationHeaderText.setText(group.location)
            binding.rvGroupedReferee.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = RefereeMainItemAdapter(onClick, group.groupedReferee)
                addItemDecoration(
                    DividerItemDecoration(binding.root.context,
                        DividerItemDecoration.VERTICAL)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefereeMainAdapter.RefereeViewHolder {
        return RefereeViewHolder(RefereeRecyclerviewHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RefereeMainAdapter.RefereeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //
    fun setList(referee: List<RefereeInfoGroup>) {
        items = referee
    }
}