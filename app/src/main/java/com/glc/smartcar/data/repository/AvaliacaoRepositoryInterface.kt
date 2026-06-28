package com.glc.smartcar.data.repository

import com.glc.smartcar.core.Result
import com.glc.smartcar.data.model.avaliacao.AvaliacaoRequest
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse

interface AvaliacaoRepositoryInterface {
    suspend fun listarAvaliacoes(): Result<List<AvaliacaoResponse>>
    suspend fun buscarAvaliacao(id: Long): Result<AvaliacaoResponse>
    suspend fun criarAvaliacao(request: AvaliacaoRequest): Result<AvaliacaoResponse>
    suspend fun desativarAvaliacao(id: Long): Result<AvaliacaoResponse>
}