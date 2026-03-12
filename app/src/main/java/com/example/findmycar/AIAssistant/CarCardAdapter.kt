package com.example.findmycar.aiassistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findmycar.data.MarketcheckListing
import com.example.findmycar.databinding.ItemChatCarCardBinding
import java.util.Locale

class CarCardAdapter(private val onCarClick: (MarketcheckListing) -> Unit) :
    ListAdapter<MarketcheckListing, CarCardAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemChatCarCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listing: MarketcheckListing, onCarClick: (MarketcheckListing) -> Unit) {
            binding.textViewCarName.text = listing.heading
            binding.textViewCarPrice.text = String.format(Locale.US, "$%,.0f", listing.price)
            binding.textViewCarLocation.text = listing.dealer?.let { "${it.city}, ${it.state}" } ?: "Location N/A"
            
            binding.buttonViewDetails.setOnClickListener { onCarClick(listing) }
            binding.root.setOnClickListener { onCarClick(listing) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatCarCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onCarClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<MarketcheckListing>() {
        override fun areItemsTheSame(oldItem: MarketcheckListing, newItem: MarketcheckListing): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MarketcheckListing, newItem: MarketcheckListing): Boolean {
            return oldItem == newItem
        }
    }
}
