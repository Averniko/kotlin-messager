package com.averniko.messager.data.conversations

import com.averniko.messager.data.model.Conversation
import com.averniko.messager.data.model.LoggedInUser
import com.averniko.messager.data.model.Message
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import com.averniko.messager.data.Result
import com.averniko.messager.data.login.LoginDataSource
import com.averniko.messager.data.login.LoginRepository

class ConversationsDataSource {

    private val conversationsURL = "http://192.168.0.101:8081/api/dialogs"
    val messagesURL = "http://localhost:8080/api/messages"

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
                                text = lastMessage.getString("text")
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
//        var text: String
//        val connection = URL(url).openConnection() as HttpURLConnection
//        connection.requestMethod = "POST"
//        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
//        val jsonInputString = "{\"login\": \"${username}\", \"password\": \"${password}\"}"
//        try {
//            val wr = OutputStreamWriter(connection.outputStream);
//            wr.write(jsonInputString)
//            wr.flush()
//            connection.connect()
//            text =
//                connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
//            val token = JSONObject(text).getString("Token")
//            return Result.Success(LoggedInUser(username, token))
//        } catch (e: Exception) {
//            println(e)
//            return Result.Error(e)
//        } finally {
//            connection.disconnect()
//        }
        return Result.Success(emptyList())
    }
}