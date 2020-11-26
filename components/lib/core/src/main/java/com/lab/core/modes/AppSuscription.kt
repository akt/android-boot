package com.lab.core.modes

import com.lab.core.R
import com.lab.core.resource.StringsProvider

class AppSuscription(
    val stringsProvider: StringsProvider
) {

    fun getUserSuscription(): String {
        return stringsProvider.getString(R.string.app_name)
    }


}

