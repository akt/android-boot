package com.aboot.app

import android.app.Application
import com.aboot.di.ApplicationComponent
import com.aboot.di.DaggerApplicationComponent
import com.aboot.di.provider.ApplicationComponentProvider
import com.biz.login.di.DaggerLoginComponent
import com.biz.login.di.LoginComponent
import com.biz.login.di.provider.LoginComponentProvider
import com.lab.core.di.CoreModule

class MyApp : Application(),
        LoginComponentProvider,
        ApplicationComponentProvider {

    private val coreModule: CoreModule by lazy {
        CoreModule(this)
    }


    override fun getLoginComponent(): LoginComponent {
        return DaggerLoginComponent.builder()
                .coreModule(coreModule)
                .build()
    }

    override fun getApplicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder()
                .coreModule(coreModule)
                .build()
    }


}