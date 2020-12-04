package com.example.bizinterface.user

import android.content.Context
import com.example.bizinterface.IBizModule

interface IUserBizModule : IBizModule{

    fun login(context:Context)

}
