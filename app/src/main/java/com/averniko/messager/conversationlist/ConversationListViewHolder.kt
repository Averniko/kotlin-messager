package com.averniko.messager.conversationlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.averniko.messager.R
import com.averniko.messager.data.model.Conversation

class ConversationListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val interlocutorTextView: TextView = itemView.findViewById(R.id.interlocutor)
    val lastMessageTextView: TextView = itemView.findViewById(R.id.message_text)

    companion object {

        fun from(parent: ViewGroup) : ConversationListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_conversation, parent, false)
            return ConversationListViewHolder(view)
        }
    }

    fun bind(item: Conversation) {
        interlocutorTextView.text = item.interlocutor
        lastMessageTextView.text = item.getLastMessage()?.text
    }
}