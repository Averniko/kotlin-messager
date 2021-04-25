package com.averniko.messager.conversation

import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.averniko.messager.R
import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.Message

class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView.findViewById(R.id.message_text)
    val layout: ConstraintLayout = itemView.findViewById(R.id.message_layout)

    companion object {

        fun from(parent: ViewGroup) : ConversationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_message, parent, false)
            return ConversationViewHolder(view)
        }
    }

    fun bind(item: Message) {
        textView.text = item.text
        if (item.isSend) {
            textView.setTextColor(Color.BLACK)
        } else {
            textView.setTextColor(Color.GREEN)
        }
    }
}