package com.aboot.di.provider

import com.aboot.di.ApplicationComponent

interface ApplicationComponentProvider {

    fun getApplicationComponent(): ApplicationComponent
}