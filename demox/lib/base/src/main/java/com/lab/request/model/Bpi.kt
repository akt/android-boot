package com.lab.request.model


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Bpi(
    @Expose
    @SerializedName("EUR") var eUR: EUR = EUR(),
    @Expose
    @SerializedName("GBP") var gBP: GBP = GBP(),
    @Expose
    @SerializedName("USD") var uSD: USD = USD()
)
