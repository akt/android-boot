package com.biz.login.di

import com.biz.login.ui.LoginActivity
import com.lab.core.di.CoreModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoginModule::class, CoreModule::class])
interface LoginComponent {

    fun inject(loginActivity: LoginActivity)
}