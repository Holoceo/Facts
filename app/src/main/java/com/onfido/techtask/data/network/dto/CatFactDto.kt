package com.onfido.techtask.data.network.dto

import com.google.gson.annotations.SerializedName

data class CatFactDto(
    @SerializedName("_id") val id: String,
    @SerializedName("status") val status: StatusDto,
    @SerializedName("text") val text: String,
    @SerializedName("createdAt") val createdAt: String
)
