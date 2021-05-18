package com.averniko.messenger.data.login

import com.averniko.messenger.data.Result
import com.averniko.messenger.data.model.LoggedInUser
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginDataSource {

    val url = "http://192.168.0.101:8081/api/login"

    fun login(username: String, password: String): Result<LoggedInUser> {
        var text: String
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        val jsonInputString = "{\"login\": \"${username}\", \"password\": \"${password}\"}"
        try {
            val wr = OutputStreamWriter(connection.outputStream);
            wr.write(jsonInputString)
            wr.flush()
            connection.connect()
            text =
                connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            val token = JSONObject(text).getString("Token")
            return Result.Success(LoggedInUser(username, token))
        } catch (e: Exception) {
            println(e)
            return Result.Error(e)
        }
        finally {
            connection.disconnect()
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}