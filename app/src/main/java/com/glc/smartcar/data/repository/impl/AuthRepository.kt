package com.glc.smartcar.data.repository.impl

import com.glc.smartcar.data.api.ApiService
import com.glc.smartcar.data.model.auth.CadastroRequest
import com.glc.smartcar.data.model.auth.LoginRequest
import com.glc.smartcar.data.repository.AuthRepositoryInterface
import com.glc.smartcar.data.repository.Result
import com.glc.smartcar.data.repository.TokenManager

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepositoryInterface {
    override suspend fun login(
        email: String,
        senha: String
    ): Result<Unit> {
        return try {
            val response = apiService.fazerLogin(LoginRequest(email, senha))

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                tokenManager.salvarToken(token)
                Result.Success(Unit)
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "E-mail ou senha incorretos."
                    else -> "Erro ao fazer login. Tente novamente."
                }
                Result.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }

    override suspend fun cadastrar(
        nome: String,
        email: String,
        senha: String
    ): Result<Unit> {
        return try {
            val response = apiService.cadastrarUsuario(CadastroRequest(nome, email, senha))

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorMessage = if (response.code() == 409) {
                    "Este e-mail já está em uso."
                } else {
                    "Erro ao cadastrar usuário."
                }
                Result.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            Result.Error("Falha na conexão: ${e.localizedMessage}")
        }
    }
}