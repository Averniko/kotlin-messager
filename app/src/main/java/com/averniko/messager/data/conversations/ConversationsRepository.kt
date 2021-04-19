package com.averniko.messager.data.conversations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.averniko.messager.data.Result
import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.Message

class ConversationsRepository(val dataSource: ConversationsDataSource) {

    private val _conversationList = MutableLiveData<List<Conversation>>()
    private val conversationList: LiveData<List<Conversation>> = _conversationList

    init {
        _conversationList.value = emptyList()
    }

    private fun loadConversations(): Result<List<Conversation>> {
        val result = dataSource.load()

        if (result is Result.Success<List<Conversation>>) {
            setConversationList(result.data)
        }

        return result
    }

    fun loadMessages(interlocutor: String): Result<List<Message>> {
        val result = dataSource.loadMessages(interlocutor)

        if (result is Result.Success<List<Message>>) {
            val resultList = conversationList.value?.map { conversation ->
                if (conversation.interlocutor == interlocutor) {
                    conversation.messages = result.data
                    return@map conversation
                }
                return@map conversation
            }
            setConversationList(resultList)
        }

        return result
    }

    fun getAllConversations(): LiveData<List<Conversation>> {
        loadConversations()
        return this.conversationList
    }

    private fun setConversationList(conversationList: List<Conversation>?) {
        this._conversationList.value = conversationList
    }
}