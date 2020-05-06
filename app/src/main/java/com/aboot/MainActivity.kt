package com.aboot

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aboot.di.provider.ApplicationComponentProvider
import com.biz.login.ui.LoginActivity
import com.lab.core.modes.AppSuscription
import com.lab.db.ui.BookActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appSuscription: AppSuscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as ApplicationComponentProvider)
            .getApplicationComponent()
            .inject(this)

        showSuscription()

    }

    private fun showSuscription() {
        subscriptionText.text = appSuscription.getUserSuscription()
    }


    fun onClick(v: View) {
        when (v.id) {
            R.id.to_db_activity -> startActivity(Intent(this, BookActivity::class.java))
            R.id.toLogin -> startActivity(Intent(this, LoginActivity::class.java))
        }
    }

}
