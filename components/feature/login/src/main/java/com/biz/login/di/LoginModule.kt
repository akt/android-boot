package com.biz.login.di

import com.biz.login.usecase.LoginUseCase
import com.lab.core.resource.StringsProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule{

    @Provides
    @Singleton
    fun loginUseCase(
        stringsProvider: StringsProvider
    ): LoginUseCase =
            LoginUseCase(stringsProvider)

}