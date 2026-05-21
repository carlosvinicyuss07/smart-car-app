package com.glc.smartcar.data.repository.impl

import com.glc.smartcar.data.api.ApiService
import com.glc.smartcar.data.model.avaliacao.AvaliacaoRequest
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse
import com.glc.smartcar.data.repository.AvaliacaoRepositoryInterface
import com.glc.smartcar.data.repository.Result

class AvaliacaoRepository(
    private val apiService: ApiService
) : AvaliacaoRepositoryInterface {

    override suspend fun listarAvaliacoes(): Result<List<AvaliacaoResponse>> {
        return try {
            val response = apiService.listarAvaliacoes()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao buscar histórico de avaliações.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun criarAvaliacao(request: AvaliacaoRequest): Result<AvaliacaoResponse> {
        return try {
            val response = apiService.criarAvaliacao(request)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao processar nova avaliação.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun desativarAvaliacao(id: Long): Result<AvaliacaoResponse> {
        return try {
            val response = apiService.desativarAvaliacao(id)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                val msg = when (response.code()) {
                    403 -> "Você não tem permissão para remover esta avaliação."
                    404 -> "Avaliação não encontrada."
                    else -> "Erro ao desativar avaliação."
                }
                Result.Error(msg, response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }
}