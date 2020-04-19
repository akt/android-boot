package com.biz.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var sumUseCase: SumUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        DaggerLoginComponent.builder()
            .loginModule(LoginModule(application))
            .coreModule(CoreModule(application))
            .build().inject(this)
    }


    fun onClick(v: View) {

    }
}