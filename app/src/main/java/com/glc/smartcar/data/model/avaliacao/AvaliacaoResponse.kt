package com.glc.smartcar.data.model.avaliacao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvaliacaoResponse(
    val id: Long,
    val usuarioId: Long,
    val fipeId: String,
    val precoDesejado: Double,
    val precoFipe: Double,
    val statusResultado: String,
    @SerialName("criado_a") val criadoA: String,
    val conservacao: String,
    val historicoAtivo: String,
    val notasPessoais: String,
    val avaliacaoIA: String? = null
)
