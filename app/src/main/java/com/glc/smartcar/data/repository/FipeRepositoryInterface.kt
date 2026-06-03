package com.glc.smartcar.data.repository

import com.glc.smartcar.core.Result
import com.glc.smartcar.data.model.fipe.FipeDetailResponse
import com.glc.smartcar.data.model.fipe.FipeItemResponse

interface FipeRepositoryInterface {
    suspend fun listarMarcas(): Result<List<FipeItemResponse>>
    suspend fun listarModelos(brandId: String): Result<List<FipeItemResponse>>
    suspend fun listarAnos(brandId: String, modelId: String): Result<List<FipeItemResponse>>
    suspend fun obterPrecoFipe(brandId: String, modelId: String, yearId: String): Result<FipeDetailResponse>
}