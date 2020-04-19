package com.biz.login

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule(
    val application: Application
) {

    @Provides
    @Singleton
    fun stringsProvider() = StringsProvider(application)


}