package com.lab.core.di

import com.lab.core.modes.AppSuscription
import com.lab.core.resource.StringsProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [CoreModule::class])
class SuscriptionModule {

    @Provides
    @Singleton
    fun appSuscription(
        stringsProvider: StringsProvider
    ): AppSuscription = AppSuscription(stringsProvider)

}