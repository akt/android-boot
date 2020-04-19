package com.biz.login

import android.app.Application
import androidx.annotation.StringRes


class StringsProvider(
    val application: Application
) {

    fun getString(@StringRes id: Int): String = application.getString(id)

}