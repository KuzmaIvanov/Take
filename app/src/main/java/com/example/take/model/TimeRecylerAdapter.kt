package com.example.take.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.take.databinding.ItemTimeBinding

class TimeRecylerAdapter(
    private val timeList: List<String>
): RecyclerView.Adapter<TimeRecylerAdapter.TimeViewHolder>() {

    class TimeViewHolder(
        val binding: ItemTimeBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTimeBinding.inflate(inflater, parent, false)
        return TimeRecylerAdapter.TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = timeList[position]
        with(holder.binding) {
            holder.itemView.tag = time
            itemTimeTextView.text = time
        }
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

}