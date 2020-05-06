package com.biz.login.di.provider

import com.biz.login.di.LoginComponent

interface LoginComponentProvider{

    fun getLoginComponent() : LoginComponent

}