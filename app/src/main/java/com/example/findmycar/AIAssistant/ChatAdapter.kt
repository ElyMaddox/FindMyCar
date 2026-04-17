package com.example.findmycar.aiassistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findmycar.R
import com.example.findmycar.data.MarketcheckListing

class ChatAdapter(private val onCarClick: (MarketcheckListing) -> Unit) : 
    ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    // Shared pool for nested horizontal car lists to improve memory usage
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layout = if (viewType == VIEW_TYPE_USER) {
            R.layout.item_chat_user
        } else {
            R.layout.item_chat_ai
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatViewHolder(view, onCarClick, viewPool)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChatViewHolder(
        itemView: View, 
        private val onCarClick: (MarketcheckListing) -> Unit,
        private val sharedPool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val messageBody: TextView = itemView.findViewById(R.id.textView_message_body)
        private val recyclerViewCars: RecyclerView? = itemView.findViewById(R.id.recyclerView_cars)
        
        // Optimize: Initialize adapter once per ViewHolder instead of per bind
        private val carAdapter by lazy { CarCardAdapter(onCarClick) }

        init {
            // Setup nested RecyclerView once
            recyclerViewCars?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                    initialPrefetchItemCount = 4 // Optimization: Prefetch items during scrolling
                }
                adapter = carAdapter
                setRecycledViewPool(sharedPool)
                setHasFixedSize(true)
                // Performance: Reduce scroll jitter
                setItemViewCacheSize(20)
            }
        }

        fun bind(message: ChatMessage) {
            messageBody.text = message.content
            
            if (recyclerViewCars != null) {
                if (!message.carListings.isNullOrEmpty()) {
                    recyclerViewCars.visibility = View.VISIBLE
                    carAdapter.submitList(message.carListings)
                } else {
                    recyclerViewCars.visibility = View.GONE
                }
            }
        }
    }

    class ChatDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
