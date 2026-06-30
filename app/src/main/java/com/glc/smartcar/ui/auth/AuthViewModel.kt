package com.glc.smartcar.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glc.smartcar.core.Result
import com.glc.smartcar.data.repository.AuthRepositoryInterface
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<AuthUiSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.OnNomeChanged -> {
                _uiState.update { it.copy(nome = event.value) }
            }

            is AuthUiEvent.OnEmailChanged -> {
                _uiState.update { it.copy(email = event.value) }
            }

            is AuthUiEvent.OnSenhaChanged -> {
                _uiState.update { it.copy(senha = event.value) }
            }

            is AuthUiEvent.OnConfirmarSenhaChanged -> {
                _uiState.update { it.copy(confirmarSenha = event.value) }
            }

            AuthUiEvent.OnToggleModeClick -> {
                _uiState.update {
                    it.copy(
                        isLoginMode = !it.isLoginMode,
                        nome = "",
                        email = "",
                        senha = "",
                        confirmarSenha = "",
                        formErrors = AuthFormErrors()
                    )
                }
            }

            AuthUiEvent.OnSubmitClick -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val state = _uiState.value

        val nomeError =
            if (!state.isLoginMode && state.nome.isBlank()) "Nome é obrigatório" else null
        val emailError = if (state.email.isBlank()) "Email é obrigatório" else null
        val senhaError = if (state.senha.isBlank()) "Senha é obrigatória" else null
        val confirmarSenhaError = if (!state.isLoginMode) {
            if (state.confirmarSenha.isBlank()) "Confirmar senha é obrigatório"
            else if (state.senha != state.confirmarSenha) "As senhas não coincidem"
            else null
        } else null

        if (nomeError != null || emailError != null || senhaError != null || confirmarSenhaError != null) {
            _uiState.update {
                it.copy(
                    formErrors = AuthFormErrors(
                        nomeError = nomeError,
                        emailError = emailError,
                        senhaError = senhaError,
                        confirmarSenhaError = confirmarSenhaError
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = if (state.isLoginMode) {
                repository.login(state.email, state.senha)
            } else {
                repository.cadastrar(state.nome, state.email, state.senha)
            }

            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success<*> -> {
                    if (state.isLoginMode) {
                        _sideEffects.send(AuthUiSideEffect.NavigateToHome)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoginMode = true,
                                nome = "",
                                confirmarSenha = "",
                                formErrors = AuthFormErrors()
                            )
                        }
                        _sideEffects.send(AuthUiSideEffect.ShowSnackbar("Cadastro realizado com sucesso!"))
                    }
                }

                is Result.Error -> {
                    _sideEffects.send(AuthUiSideEffect.ShowSnackbar(result.message))
                }
            }
        }
    }
}