package com.averniko.messager.conversationlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.averniko.login.ui.login.LoginResult

import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.data.model.Conversation
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