package com.aboot.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aboot.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_webview.*

/**
 * Created by ak on 2021/3/11.
 */
class WebviewActivity: AppCompatActivity() {


    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        wb.loadUrl("file:///android_asset/x.html");


    }



}
