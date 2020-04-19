package com.biz.login

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LoginModule::class, CoreModule::class])
interface LoginComponent {

    fun inject(loginActivity: LoginActivity)
}