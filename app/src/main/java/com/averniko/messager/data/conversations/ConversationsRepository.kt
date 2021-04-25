package com.averniko.messager.data.conversations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.averniko.messager.data.Result
import com.averniko.messager.data.login.LoginDataSource
import com.averniko.messager.data.login.LoginRepository
import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.Message

class ConversationsRepository private constructor(val dataSource: ConversationsDataSource) {

    companion object {
        private var instance: ConversationsRepository? = null

        fun getInstance(dataSource: ConversationsDataSource): ConversationsRepository {
            if (instance == null)
                instance = ConversationsRepository(dataSource)

            return instance!!
        }
    }

    private val _conversationList = MutableLiveData<List<Conversation>>()
    private val conversationList: LiveData<List<Conversation>> = _conversationList

    fun loadConversations(): Result<List<Conversation>> {
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
        return this.conversationList
    }

    fun addConversation(interlocutor: String) {
        if (dataSource.addContact(interlocutor) is Result.Error) {
            return
        }
        setConversationList(conversationList.value?.plus(Conversation(interlocutor, emptyList())))
    }

    fun sendMessage(message: Message) {
        if (dataSource.sendMessage(message) is Result.Error) {
            return
        }
        val resultList = conversationList.value?.map { conversation ->
            if (conversation.interlocutor == message.receiver) {
                conversation.messages = conversation.messages.plus(message)
                return@map conversation
            }
            return@map conversation
        }
        setConversationList(resultList)
    }

    fun addReceivedMessage(message: Message) {
        val resultList = conversationList.value?.map { conversation ->
            if (conversation.interlocutor == message.sender) {
                conversation.messages = conversation.messages.plus(message)
                return@map conversation
            }
            return@map conversation
        }
        setConversationList(resultList)
    }

    private fun setConversationList(conversationList: List<Conversation>?) {
        this._conversationList.postValue(conversationList)
    }
}