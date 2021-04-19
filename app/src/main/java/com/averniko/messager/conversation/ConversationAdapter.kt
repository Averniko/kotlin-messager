package com.averniko.messager.conversation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.Message

class ConversationAdapter : RecyclerView.Adapter<ConversationViewHolder>() {

    var data = listOf<Message>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind((data[position]))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}