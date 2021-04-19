package com.averniko.messager.conversation

import androidx.lifecycle.ViewModel

import com.averniko.messager.data.conversations.ConversationsRepository

class ConversationViewModel(private val interlocutor: String, private val conversationsRepository: ConversationsRepository) : ViewModel() {

    init {
        conversationsRepository.loadMessages(interlocutor)
    }
    val conversation = conversationsRepository.getAllConversations()
}