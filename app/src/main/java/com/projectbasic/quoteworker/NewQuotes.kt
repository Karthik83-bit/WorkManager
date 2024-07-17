package com.projectbasic.quoteworker


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class NewQuotes(
    @SerializedName("author")
    val author: String?, // Ali ibn Abi Talib (R.A)
    @SerializedName("id")
    val id: Int?, // 1000
    @SerializedName("quote")
    val quote: String? // During civil disturbance adopt such an attitude that people do not attach any importance to you - they neither burden you with complicated affairs, nor try to derive any advantage out of you.
)