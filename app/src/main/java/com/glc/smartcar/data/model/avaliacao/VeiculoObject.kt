package com.glc.smartcar.data.model.avaliacao

import kotlinx.serialization.Serializable

@Serializable
data class VeiculoObject(
    val marca: String,
    val modelo: String,
    val ano: Int,
    val combustivel: String
)
