package com.glc.smartcar.data.model.avaliacao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvaliacaoResponse(
    val id: Long,
    val fipeId: String,
    val veiculo: VeiculoObject? = null,
    val precoDesejado: Double,
    val precoFipe: Double,
    val variacao: Double,
    val statusResultado: String,
    @SerialName("criado_a") val criadoA: String,
    val conservacao: String,
    val historicoAtivo: String,
    val notasPessoais: String,
    val avaliacaoIA: String? = null
)
