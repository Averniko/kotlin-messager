package com.averniko.messager.data.conversations

import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.Message
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import com.averniko.messager.data.Result
import com.averniko.messager.data.login.LoginDataSource
import com.averniko.messager.data.login.LoginRepository
import com.averniko.messager.data.model.LoggedInUser
import java.io.OutputStreamWriter
import java.net.URLEncoder

class ConversationsDataSource {

    private val conversationsURL = "http://192.168.0.101:8081/api/dialogs"
    private val messagesURL = "http://192.168.0.101:8081/api/messages"
    private val addContactURL = "http://192.168.0.101:8081/api/contact/add"
    private val sendMessageURL = "http://192.168.0.101:8081/api/message/send"

    val loginRepository: LoginRepository = LoginRepository.getInstance(LoginDataSource())

    fun load(): Result<List<Conversation>> {
        var text: String
        val connection = URL(conversationsURL).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.setRequestProperty(
            "Authorization",
            "Token ${loginRepository.user?.token.toString()}"
        )
        try {
            connection.connect()
            text =
                connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            var dialogsList = JSONObject(text).getJSONArray("dialogs")
                ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
                ?.map { it ->
                    val lastMessage = it.optJSONObject("lastMessage")
                    var messageList = emptyList<Message>()
                    if (lastMessage != null) {
                        messageList = listOf(
                            Message(
                                sender = lastMessage.getString("sender"),
                                receiver = lastMessage.getString("receiver"),
                                text = lastMessage.getString("text"),
                                isSend = loginRepository.user?.login == lastMessage.getString("sender")
                            )
                        )
                    }
                    Conversation(
                        it.getString("login"),
                        messageList
                    )
                }
            if (dialogsList == null)
                dialogsList = emptyList()
            return Result.Success(dialogsList)
        } catch (e: Exception) {
            println(e)
            return Result.Error(e)
        } finally {
            connection.disconnect()
        }
    }

    fun loadMessages(interlocutor: String): Result<List<Message>> {
        var text: String
        var queryParam =
            URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(interlocutor, "UTF-8")
        val connection = URL("${messagesURL}?" + queryParam).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.setRequestProperty(
            "Authorization",
            "Token ${loginRepository.user?.token.toString()}"
        )
        try {
            connection.connect()
            text =
                connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            var messagesList = JSONObject(text).getJSONArray("messages")
                ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
                ?.map { it ->
                    Message(
                        sender = it.getString("from"),
                        receiver = it.getString("to"),
                        text = it.getString("text"),
                        isSend = loginRepository.user?.login == it.getString("from")
                    )
                }
            if (messagesList == null)
                messagesList = emptyList()
            return Result.Success(messagesList)
        } catch (e: Exception) {
            println(e)
            return Result.Error(e)
        } finally {
            connection.disconnect()
        }
    }

    fun addContact(interlocutor: String): Result<String> {
        val connection = URL(addContactURL).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.setRequestProperty(
            "Authorization",
            "Token ${loginRepository.user?.token.toString()}"
        )
        val jsonInputString = "{\"login\": \"${interlocutor}\"}"
        try {
            val wr = OutputStreamWriter(connection.outputStream);
            wr.write(jsonInputString)
            wr.flush()
            connection.connect()
            val text =
                connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            return Result.Success(interlocutor)
        } catch (e: Exception) {
            println(e)
            return Result.Error(e)
        } finally {
            connection.disconnect()
        }
    }

    fun sendMessage(message: Message): Result<String> {
        val connection = URL(sendMessageURL).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.setRequestProperty(
            "Authorization",
            "Token ${loginRepository.user?.token.toString()}"
        )
        val jsonInputString = "{\"to\": \"${message.receiver}\", \"text\":\"${message.text}\"}"
        try {
            val wr = OutputStreamWriter(connection.outputStream);
            wr.write(jsonInputString)
            wr.flush()
            connection.connect()

            val text =
                connection.inputStream.use { it.reader().use { reader -> reader.readText() } }

            return Result.Success(message.text)
        } catch (e: Exception) {
            println(e)
            return Result.Error(e)
        } finally {
            connection.disconnect()
        }
    }
}