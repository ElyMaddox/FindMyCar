package com.example.findmycar.cardetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findmycar.data.CarDetailItem
import com.example.findmycar.databinding.ItemCarDetailBinding

class CarDetailsAdapter : ListAdapter<CarDetailItem, CarDetailsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemCarDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CarDetailItem) {
            binding.textViewLabel.text = item.label
            binding.textViewValue.text = item.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCarDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CarDetailItem>() {
        override fun areItemsTheSame(oldItem: CarDetailItem, newItem: CarDetailItem): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: CarDetailItem, newItem: CarDetailItem): Boolean {
            return oldItem == newItem
        }
    }
}
