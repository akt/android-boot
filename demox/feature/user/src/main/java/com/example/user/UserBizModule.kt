package com.example.user

import android.content.Context
import android.util.Log
import com.example.bizinterface.user.IUserBizModule

class UserBizModule: IUserBizModule {
    override fun login(context: Context) {
        Log.e(this.javaClass.canonicalName, "login gogogo ==")
    }
}