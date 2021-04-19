package com.averniko.messager.data.login

import android.os.AsyncTask
import com.averniko.messager.data.Result
import com.averniko.messager.data.conversations.ConversationsDataSource
import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.data.model.LoggedInUser
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class LoginRepository(val dataSource: LoginDataSource) {

    companion object {
        private var instance: LoginRepository? = null

        fun getInstance(dataSource: LoginDataSource): LoginRepository {
            if (instance == null)
                instance = LoginRepository(dataSource)

            return instance!!
        }
    }

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}