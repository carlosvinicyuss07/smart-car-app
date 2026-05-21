package com.glc.smartcar.data.model.fipe

import kotlinx.serialization.Serializable

@Serializable
data class FipeDetailResponse(
    val price: String,
    val brand: String,
    val model: String,
    val modelYear: Int,
    val fuel: String,
    val codeFipe: String,
    val referenceMonth: String
)
