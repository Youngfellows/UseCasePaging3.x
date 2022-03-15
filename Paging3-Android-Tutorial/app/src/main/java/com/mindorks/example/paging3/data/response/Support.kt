package com.mindorks.example.paging3.data.response

import com.squareup.moshi.Json

data class Support(
//    @Json(name = "company")
//    val company: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "url")
    val url: String
)