package com.glc.smartcar.data.model.usuario

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioPerfilResponse(
    val id: Long,
    val nome: String,
    val email: String,
    @SerialName("criado_a") val criadoA: String
)
