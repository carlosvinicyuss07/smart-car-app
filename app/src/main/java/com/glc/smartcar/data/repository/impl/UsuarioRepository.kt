package com.glc.smartcar.data.repository.impl

import com.glc.smartcar.data.api.ApiService
import com.glc.smartcar.data.local.TokenManager
import com.glc.smartcar.data.model.usuario.UsuarioPerfilResponse
import com.glc.smartcar.data.repository.UsuarioRepositoryInterface
import com.glc.smartcar.core.Result

class UsuarioRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : UsuarioRepositoryInterface {

    override suspend fun obterPerfilUsuario(): Result<UsuarioPerfilResponse> {
        return try {
            val response = apiService.obterPerfilUsuario()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Erro ao obter perfil do usuário.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun deletarUsuario(): Result<Unit> {
        return try {
            val response = apiService.deletarUsuario()
            if (response.isSuccessful) {
                tokenManager.limparToken()
                Result.Success(Unit)
            } else {
                Result.Error("Erro ao deletar usuário.", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }
}