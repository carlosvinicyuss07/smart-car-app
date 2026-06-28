package com.glc.smartcar.data.repository.impl

import com.glc.smartcar.data.api.ApiService
import com.glc.smartcar.data.model.fipe.FipeDetailResponse
import com.glc.smartcar.data.model.fipe.FipeItemResponse
import com.glc.smartcar.data.repository.FipeRepositoryInterface
import com.glc.smartcar.core.Result

class FipeRepository(
    private val apiService: ApiService
) : FipeRepositoryInterface {

    override suspend fun listarMarcas(): Result<List<FipeItemResponse>> {
        return try {
            val response = apiService.listarMarcas()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao carregar marcas.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun listarModelos(brandId: String): Result<List<FipeItemResponse>> {
        return try {
            val response = apiService.listarModelos(brandId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao carregar modelos.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun listarAnos(brandId: String, modelId: String): Result<List<FipeItemResponse>> {
        return try {
            val response = apiService.listarAnos(brandId, modelId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao carregar anos.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun obterPrecoFipe(brandId: String, modelId: String, yearId: String): Result<FipeDetailResponse> {
        return try {
            val response = apiService.obterPrecoFipe(brandId, modelId, yearId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao obter preço FIPE.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }
}