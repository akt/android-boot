package com.biz.login.usecase

import com.biz.login.R
import com.lab.core.resource.StringsProvider

class LoginUseCase(private val stringsProvider: StringsProvider) {

    fun execute(username: String, password: String): Result {
        return if (username.isNotEmpty() && password.isNotEmpty()) {
            Result.Success(stringsProvider.getString(R.string.success_message))
        } else {
            Result.Failure(stringsProvider.getString(R.string.fail_message))
        }
    }

    sealed class Result {
        class Success(private val result: String) : Result(){
            override fun toString(): String {
                return result
            }
        }
        class Failure(private val message: String) : Result(){
            override fun toString(): String {
                return message
            }
        }
    }

}