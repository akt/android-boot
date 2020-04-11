package com.biz.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {

    lateinit var useCase: SumUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        useCase = SumUseCase()
    }


    fun onClick(v: View) {

    }
}