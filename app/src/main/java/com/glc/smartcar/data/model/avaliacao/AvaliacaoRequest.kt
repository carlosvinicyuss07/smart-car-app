package com.glc.smartcar.data.model.avaliacao

import kotlinx.serialization.Serializable

@Serializable
data class AvaliacaoRequest(
    val kmsRodados: Double,
    val brandId: String,
    val modelId: String,
    val yearId: String,
    val precoDesejado: Double,
    val notasPessoais: String,
    val conservacao: String
)
