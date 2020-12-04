package com.example.bizinterface

import java.util.concurrent.ConcurrentHashMap

object ModuleManager {

    private val moduleFactoryMap = ConcurrentHashMap<String, IBizModuleProvider<out IBizModule>>()

    fun <T : IBizModule, S : T> injectModuleProvider(moduleClass: Class<T>, provider: IBizModuleProvider<S>) {
        moduleFactoryMap[moduleClass.canonicalName!!] = ScopedProvider.create(provider)
    }

    fun <T : IBizModule> getModule(moduleClass: Class<T>): T {
        return moduleFactoryMap[moduleClass.canonicalName!!]?.provideModule() as T
    }
}