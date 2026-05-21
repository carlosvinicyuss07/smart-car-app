package com.glc.smartcar.data.model.fipe

import kotlinx.serialization.Serializable

@Serializable
data class FipeItemResponse(
    val name: String,
    val code: String
)
