package com.averniko.messager.conversationlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.averniko.messager.data.model.Conversation

class ConversationListAdapter(var onItemClick: ((Conversation) -> Unit)? = null) : RecyclerView.Adapter<ConversationListViewHolder>() {

    var data = listOf<Conversation>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationListViewHolder {
        return ConversationListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ConversationListViewHolder, position: Int) {
        holder.bind((data[position]))
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}