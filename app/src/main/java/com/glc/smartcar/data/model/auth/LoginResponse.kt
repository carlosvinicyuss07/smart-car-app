package com.glc.smartcar.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String
)
