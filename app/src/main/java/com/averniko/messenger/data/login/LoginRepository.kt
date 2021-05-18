package com.averniko.messenger.data.login

import com.averniko.messenger.data.Result
import com.averniko.messenger.data.model.LoggedInUser

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