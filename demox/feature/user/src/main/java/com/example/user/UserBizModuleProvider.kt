package com.example.user

import com.example.bizinterface.IBizModuleProvider
import com.example.bizinterface.user.IUserBizModule

class UserBizModuleProvider : IBizModuleProvider<IUserBizModule> {
    override fun provideModule(): IUserBizModule {
        return UserBizModule()
    }

}