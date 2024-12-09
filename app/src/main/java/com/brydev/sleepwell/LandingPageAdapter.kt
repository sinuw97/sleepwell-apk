package com.brydev.sleepwell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brydev.sleepwell.databinding.ItemPageBinding

class LandingPageAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<LandingPageAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int) {
            binding.imageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount() = images.size
}
