package com.lab.request.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CurrentPrice(
    @Expose
    @SerializedName("bpi") var bpi: Bpi = Bpi(),
    @Expose
    @SerializedName("chartName") var chartName: String = "",
    @Expose
    @SerializedName("disclaimer") var disclaimer: String = "",
    @Expose
    @SerializedName("time") var time: Time = Time()
)
