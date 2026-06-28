package com.glc.smartcar.data.repository

import com.glc.smartcar.core.Result
import com.glc.smartcar.data.model.usuario.UsuarioPerfilResponse

interface UsuarioRepositoryInterface {
    suspend fun obterPerfilUsuario(): Result<UsuarioPerfilResponse>
    suspend fun deletarUsuario(): Result<Unit>
}