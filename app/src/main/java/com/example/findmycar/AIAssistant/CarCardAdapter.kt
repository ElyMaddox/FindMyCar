package com.example.findmycar.aiassistant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Precision
import coil.size.ViewSizeResolver
import com.example.findmycar.R
import com.example.findmycar.data.MarketcheckListing
import com.example.findmycar.databinding.ItemChatCarCardBinding
import java.util.Locale

class CarCardAdapter(private val onCarClick: (MarketcheckListing) -> Unit) :
    ListAdapter<MarketcheckListing, CarCardAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemChatCarCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listing: MarketcheckListing, onCarClick: (MarketcheckListing) -> Unit) {
            val formattedPrice = String.format(Locale.US, "$%,.0f", listing.price)
            val location = listing.dealer?.let { "${it.city}, ${it.state}" } ?: "Location N/A"
            
            binding.textViewCarName.text = listing.heading
            binding.textViewCarPrice.text = formattedPrice
            binding.textViewCarLocation.text = location
            
            // Advanced Accessibility: Dynamic Content Description
            // This provides screen reader users with specific details about the car
            // rather than a generic "Image of the car" message.
            binding.imageViewCar.contentDescription = itemView.context.getString(
                R.string.car_card_content_description,
                listing.heading,
                formattedPrice,
                location
            )
            
            // Load image with downsampling optimization
            val imageUrl = listing.media?.photo_links?.firstOrNull()
            binding.imageViewCar.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_background)
                size(ViewSizeResolver(binding.imageViewCar))
                precision(Precision.EXACT)
            }
            
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
