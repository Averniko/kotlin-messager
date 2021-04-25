package com.averniko.messager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.averniko.messager.data.conversations.ConversationsDataSource
import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.data.login.LoginDataSource
import com.averniko.messager.data.login.LoginRepository
import com.averniko.messager.data.model.Message
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginRepository: LoginRepository = LoginRepository.getInstance(LoginDataSource())
        val conversationsRepository: ConversationsRepository = ConversationsRepository.getInstance(
            ConversationsDataSource()
        )

        var job = Job()

        val uiScope = CoroutineScope(Dispatchers.Default + job)

        val client = HttpClient {
            install(WebSockets)
        }

        uiScope.launch {

            client.webSocket(
                method = HttpMethod.Get,
                host = "192.168.0.101",
                port = 8081, path = "/api/ws",
                request = {
                    parameter(
                        "token",
                        loginRepository.user?.token
                    )
                }
            ) {
                while(true) {
                    val othersMessage = incoming.receive() as? Frame.Text ?: continue
                    val messageJSON = JSONObject(othersMessage.readText()).getJSONObject("message")
                    conversationsRepository.addReceivedMessage(
                        Message(
                            sender = messageJSON.optString("from"),
                            text = messageJSON.optString("text"),
                            receiver = loginRepository.user!!.login,
                            isSend = false
                        )
                    )
                }
            }
        }
    }
}