package com.averniko.messager.data.conversations

import com.averniko.messager.data.Result
import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.LoggedInUser
import com.averniko.messager.data.model.Message
import java.io.IOException

class ConversationsDataSource {

    fun load(): Result<List<Conversation>> {
        try {
            val conversationList = listOf<Conversation>(
                Conversation(interlocutor = "test1", messages = emptyList()),
                Conversation(interlocutor = "test2", messages = listOf(
                    Message("test2", "kek", "kek"),
                    Message("test2", "kek", "kek"),
                    Message("test2", "kek", "kek"),
                    Message("test2", "kek", "kek"),
                    Message("test2", "kek", "kek"),
                    Message("test2", "kek", "kek2"),
                ))
            )
            return Result.Success(conversationList)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun loadMessages(interlocutor: String): Result<List<Message>> {
        try {
            val messageList = listOf(
                Message("test1", "test2", "Привет"),
                Message("test2", "test1", "Как дела")
            )
            return Result.Success(messageList)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}