package com.glc.smartcar.data.repository

interface AuthRepositoryInterface {
    suspend fun login(email: String, senha: String): Result<Unit>
    suspend fun cadastrar(nome: String, email: String, senha: String): Result<Unit>
}