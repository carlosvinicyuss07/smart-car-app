package com.glc.smartcar.data.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CadastroResponse(
    val id: Long,
    val nome: String,
    val email: String,
    @SerialName("criado_a") val criadoA: String
)
