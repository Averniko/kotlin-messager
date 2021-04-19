package com.averniko.messager.conversationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.averniko.messager.data.conversations.ConversationsDataSource
import com.averniko.messager.data.conversations.ConversationsRepository

class ConversationListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationListViewModel::class.java)) {
            return ConversationListViewModel(
                conversationsRepository = ConversationsRepository(
                    dataSource = ConversationsDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}