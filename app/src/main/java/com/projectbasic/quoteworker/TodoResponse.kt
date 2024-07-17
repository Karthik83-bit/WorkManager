package com.projectbasic.quoteworker


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class TodoResponse(
    @SerializedName("completed")
    val completed: Boolean?, // false
    @SerializedName("id")
    val id: Int?, // 1
    @SerializedName("title")
    val title: String?, // delectus aut autem
    @SerializedName("userId")
    val userId: Int? // 1
)