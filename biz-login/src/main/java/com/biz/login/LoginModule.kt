package com.biz.login

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoginModule(val application: Application) {

    @Provides
    @Singleton
    fun application(): Application = application

    @Provides
    @Singleton
    fun sumUseCase(
        stringsProvider: StringsProvider
    ): SumUseCase = SumUseCase(stringsProvider)

}