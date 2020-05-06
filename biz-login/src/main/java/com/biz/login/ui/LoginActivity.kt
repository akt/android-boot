package com.biz.login.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.biz.login.usecase.LoginUseCase
import com.biz.login.R
import com.biz.login.di.LoginModule
import com.biz.login.di.provider.LoginComponentProvider
import com.lab.core.di.CoreModule
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginUseCase: LoginUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as LoginComponentProvider)
                .getLoginComponent()
                .inject(this)
    }


    fun onClick(v: View) {
        loginUseCase.execute("user", "password").let {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}