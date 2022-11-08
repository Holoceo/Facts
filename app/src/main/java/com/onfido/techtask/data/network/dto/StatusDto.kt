package com.onfido.techtask.data.network.dto

import com.google.gson.annotations.SerializedName

data class StatusDto(
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("sentCount") val sentCount: Int
)
