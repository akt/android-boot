package com.ak.demo
import android.app.Application
import android.os.Bundle
import com.example.bizinterface.ModuleManager
import com.example.bizinterface.user.IUserBizModule
import com.example.user.UserBizModuleProvider
import com.uc.crashsdk.export.CrashApi

class MyApp: Application(){

    override fun onCreate() {
        super.onCreate()
        val DEBUG = true
        val args = Bundle()
        args.putBoolean("mDebug", DEBUG);

        args.putString("mVersion", "1.0.0"); // 注意：put[Type] 和成员变量类型保持一致
        CrashApi.createInstanceEx(applicationContext, "demox", DEBUG, args);
        CrashApi.getInstance().addHeaderInfo("userId", "111")
        ModuleManager.injectModuleProvider(IUserBizModule::class.java, UserBizModuleProvider())
    }
}