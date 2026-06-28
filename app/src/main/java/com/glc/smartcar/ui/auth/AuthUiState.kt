package com.glc.smartcar.ui.auth

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoginMode: Boolean = true,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val confirmarSenha: String = "",
    val formErrors: AuthFormErrors = AuthFormErrors()
)

data class AuthFormErrors(
    val nomeError: String? = null,
    val emailError: String? = null,
    val senhaError: String? = null,
    val confirmarSenhaError: String? = null
)