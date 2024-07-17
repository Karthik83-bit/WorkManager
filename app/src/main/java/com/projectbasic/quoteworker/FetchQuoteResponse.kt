package com.projectbasic.quoteworker


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class FetchQuoteResponse(
    @SerializedName("author")
    val author: String?, // Joseph Prince
    @SerializedName("authorSlug")
    val authorSlug: String?, // joseph-prince
    @SerializedName("content")
    val content: String?, // As you walk in God's divine wisdom, you will surely begin to see a greater measure of victory and good success in your life.
    @SerializedName("dateAdded")
    val dateAdded: String?, // 2019-08-08
    @SerializedName("dateModified")
    val dateModified: String?, // 2023-04-14
    @SerializedName("_id")
    val id: String?, // XB54MM6tfc
    @SerializedName("length")
    val length: Int?, // 124
    @SerializedName("tags")
    val tags: List<String?>?
)