package com.brydev.sleepwell.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brydev.sleepwell.R
import com.brydev.sleepwell.model.HistoryItem

class HistoryAdapter(private val historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val qualityText: TextView = itemView.findViewById(R.id.qualityText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.qualityText.text = item.quality
        holder.dateText.text = item.date

        // Set icon based on quality
        if (item.quality == "Good Quality") {
            holder.icon.setImageResource(R.drawable.good)
        } else if (item.quality == "Low Sleep") {
            holder.icon.setImageResource(R.drawable.low)
        }
    }

    override fun getItemCount(): Int = historyList.size
}
