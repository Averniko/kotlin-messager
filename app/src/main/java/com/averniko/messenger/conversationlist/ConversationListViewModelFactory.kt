package com.averniko.messenger.conversationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.averniko.messenger.data.conversations.ConversationsDataSource
import com.averniko.messenger.data.conversations.ConversationsRepository

class ConversationListViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationListViewModel::class.java)) {
            return ConversationListViewModel(
                conversationsRepository = ConversationsRepository.getInstance(
                    dataSource = ConversationsDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}