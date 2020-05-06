package com.aboot.di

import com.aboot.MainActivity
import com.lab.core.di.SuscriptionModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SuscriptionModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

}