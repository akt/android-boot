package com.lab.request.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Time(
    @Expose
    @SerializedName("updated") var updated: String = "",
    @Expose
    @SerializedName("updatedISO") var updatedISO: String = "",
    @Expose
    @SerializedName("updateduk") var updateduk: String = ""
)
