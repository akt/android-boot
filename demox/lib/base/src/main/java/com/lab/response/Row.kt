package com.lab.response


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Row(
    @Expose
    @SerializedName("cell") var cell: Cell = Cell(),
    @Expose
    @SerializedName("id") var id: String = ""
)
