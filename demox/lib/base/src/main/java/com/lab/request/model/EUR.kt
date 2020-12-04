package com.lab.request.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class EUR(
    @Expose
    @SerializedName("code") var code: String = "",
    @Expose
    @SerializedName("description") var description: String = "",
    @Expose
    @SerializedName("rate") var rate: String = "",
    @Expose
    @SerializedName("rate_float") var rateFloat: Double = 0.0,
    @Expose
    @SerializedName("symbol") var symbol: String = ""
)
