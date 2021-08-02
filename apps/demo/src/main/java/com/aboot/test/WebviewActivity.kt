package com.aboot.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aboot.R
import com.aboot.test.Base64.getEncoder
import com.aboot.test.Base64.getUrlDecoder
import com.aboot.test.Base64.getUrlEncoder
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

        Log.d("xxx1", getUrlEncoder().encodeToString(half.toByteArray()))
        Log.d("xxx2", getUrlEncoder().encodeToString(one.toByteArray()))
        Log.d("xxx3", getUrlEncoder().encodeToString(two.toByteArray()))

        wb.loadUrl("file:///android_asset/x.html");


    }

    companion object {
        const val half = "https://www.binance.com/en/my/settings/profile?returnTo=NATIVE&kycBizMode=BASIC&kyc_bizScene=OCBS"
        const val one = "https://www.binance.com/en/my/settings/profile?returnTo=NATIVE&kycBizMode=ADVANCE&kyc_bizScene=OCBS"
        const val two = "https://www.binance.com/en/my/settings/profile/address-verification?returnTo=NATIVE&kyc_bizScene=OCBS"
    }


}
