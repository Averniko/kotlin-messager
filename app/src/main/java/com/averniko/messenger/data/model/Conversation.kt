package com.averniko.messenger.data.model

data class Conversation(
    val interlocutor: String,
    var messages: List<Message>
) {
    fun getLastMessage(): Message? {
        return messages.lastOrNull()
    }
}