package com.averniko.messenger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.averniko.messenger.data.conversations.ConversationsDataSource
import com.averniko.messenger.data.conversations.ConversationsRepository
import com.averniko.messenger.data.login.LoginDataSource
import com.averniko.messenger.data.login.LoginRepository
import com.averniko.messenger.data.model.Message
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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
                while (true) {
                    val othersMessage = incoming.receive() as? Frame.Text ?: continue
                    val messageJSON = JSONObject(othersMessage.readText()).getJSONObject("message")
                    val message = Message(
                        sender = messageJSON.optString("from"),
                        text = messageJSON.optString("text"),
                        receiver = loginRepository.user!!.login,
                        isSend = false
                    )
                    conversationsRepository.addReceivedMessage(
                        message
                    )
                    sendNotification(message)
                }
            }
        }
    }

    private fun sendNotification(message: Message) {
        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(message.sender + ": " + message.text)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(101, builder.build())
        }
    }
}