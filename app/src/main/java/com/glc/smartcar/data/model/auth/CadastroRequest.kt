package com.glc.smartcar.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class CadastroRequest(
    val nome: String,
    val email: String,
    val senha: String
)
