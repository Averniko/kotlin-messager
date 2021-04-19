package com.averniko.messager.conversation

import androidx.lifecycle.ViewModel

import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.data.model.Message

class ConversationViewModel(private val interlocutor: String, private val conversationsRepository: ConversationsRepository) : ViewModel() {

    init {
        conversationsRepository.getMessages(interlocutor)
    }
    val conversation = conversationsRepository.getAllConversations()

    fun onSendMessage(message: Message) {
        conversationsRepository.sendMessage(message)
    }
}