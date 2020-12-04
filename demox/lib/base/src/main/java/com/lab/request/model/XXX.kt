package com.lab.request.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class XXX(
    @Expose
    @SerializedName("alive") var alive: Boolean = false
)
