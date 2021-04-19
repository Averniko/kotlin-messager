package com.averniko.messager.conversationlist

import androidx.lifecycle.ViewModel

import com.averniko.messager.data.conversations.ConversationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConversationListViewModel(private val conversationsRepository: ConversationsRepository) : ViewModel() {

    private var job = Job()

    private val uiScope = CoroutineScope(Dispatchers.IO + job)

    val conversations = conversationsRepository.getAllConversations()

    init {
        uiScope.launch {
            conversationsRepository.loadConversations()
        }
    }

    fun onAddConversation(interlocutor: String) {
        uiScope.launch {
            conversationsRepository.addConversation(interlocutor)
        }
    }
}