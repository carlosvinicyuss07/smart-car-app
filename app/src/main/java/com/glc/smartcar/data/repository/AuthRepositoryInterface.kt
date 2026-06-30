package com.glc.smartcar.data.repository

import com.glc.smartcar.core.Result

interface AuthRepositoryInterface {
    suspend fun login(email: String, senha: String): Result<Unit>
    suspend fun cadastrar(nome: String, email: String, senha: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}