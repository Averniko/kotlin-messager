package com.averniko.messenger.data.model

data class Message(
    val sender: String,
    val receiver: String,
    val text: String,
    val isSend: Boolean
)