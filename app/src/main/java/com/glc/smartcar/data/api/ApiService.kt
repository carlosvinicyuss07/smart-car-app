package com.glc.smartcar.data.api

import com.glc.smartcar.data.model.auth.CadastroRequest
import com.glc.smartcar.data.model.auth.CadastroResponse
import com.glc.smartcar.data.model.auth.LoginRequest
import com.glc.smartcar.data.model.auth.LoginResponse
import com.glc.smartcar.data.model.avaliacao.AvaliacaoRequest
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse
import com.glc.smartcar.data.model.fipe.FipeDetailResponse
import com.glc.smartcar.data.model.fipe.FipeItemResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/cadastrar")
    suspend fun cadastrarUsuario(@Body request: CadastroRequest): Response<CadastroResponse>

    @POST("auth/login")
    suspend fun fazerLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST("sc")
    suspend fun criarAvaliacao(@Body request: AvaliacaoRequest): Response<AvaliacaoResponse>

    @GET("sc")
    suspend fun listarAvaliacoes(): Response<List<AvaliacaoResponse>>

    @PATCH("sc/{id}")
    suspend fun desativarAvaliacao(@Path("id") id: Long): Response<AvaliacaoResponse>

    @GET("fipe")
    suspend fun listarMarcas(): Response<List<FipeItemResponse>>

    @GET("fipe/marcas/{brandId}/modelos")
    suspend fun listarModelos(@Path("brandId") brandId: String): Response<List<FipeItemResponse>>

    @GET("fipe/marcas/{brandId}/modelos/{modelId}/anos")
    suspend fun listarAnos(
        @Path("brandId") brandId: String,
        @Path("modelId") modelId: String
    ): Response<List<FipeItemResponse>>

    @GET("fipe/marcas/{brandId}/modelos/{modelId}/anos/{yearId}")
    suspend fun obterPrecoFipe(
        @Path("brandId") brandId: String,
        @Path("modelId") modelId: String,
        @Path("yearId") yearId: String
    ): Response<FipeDetailResponse>
}