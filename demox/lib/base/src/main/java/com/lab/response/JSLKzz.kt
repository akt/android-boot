package com.lab.response


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class JSLKzz(
    @Expose
    @SerializedName("page") var page: Int = 0,
    @Expose
    @SerializedName("rows") var rows: List<Row> = listOf(),
    @Expose
    @SerializedName("total") var total: Int = 0
)
