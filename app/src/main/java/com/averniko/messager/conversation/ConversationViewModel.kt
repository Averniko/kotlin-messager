package com.averniko.messager.conversation

import androidx.lifecycle.ViewModel

import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.data.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val interlocutor: String,
    private val conversationsRepository: ConversationsRepository
) : ViewModel() {

    private var job = Job()

    private val uiScope = CoroutineScope(Dispatchers.IO + job)

    val conversations = conversationsRepository.getAllConversations()

    init {
        uiScope.launch {
            conversationsRepository.loadMessages(interlocutor)
        }
    }

    fun onSendMessage(message: Message) {
        uiScope.launch {
            conversationsRepository.sendMessage(message)
        }
    }
}