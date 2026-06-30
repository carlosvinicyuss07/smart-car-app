package com.glc.smartcar.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glc.smartcar.core.Result
import com.glc.smartcar.data.repository.AuthRepositoryInterface
import com.glc.smartcar.data.repository.UsuarioRepositoryInterface
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val usuarioRepository: UsuarioRepositoryInterface,
    private val authRepository: AuthRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffects = Channel<ProfileUiSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = usuarioRepository.obterPerfilUsuario()) {
                is Result.Success -> {
                    val usuario = result.data
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            nome = usuario.nome,
                            email = usuario.email,
                            dataCriacao = usuario.criadoA
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.OnLogoutClick -> {
                viewModelScope.launch {
                    authRepository.logout()
                    _sideEffects.send(ProfileUiSideEffect.NavigateToLogin)
                }
            }
            ProfileUiEvent.OnDeleteAccountClick -> {
                _uiState.update { it.copy(showDeleteDialog = true) }
            }
            ProfileUiEvent.OnDismissDeleteDialogClick -> {
                _uiState.update { it.copy(showDeleteDialog = false) }
            }
            ProfileUiEvent.OnConfirmDeleteAccountClick -> {
                deleteAccount()
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, error = null) }
            when (val result = usuarioRepository.deletarUsuario()) {
                is Result.Success -> {
                    _sideEffects.send(ProfileUiSideEffect.ShowToast("Conta deletada com sucesso."))
                    _sideEffects.send(ProfileUiSideEffect.NavigateToLogin)
                }
                is Result.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isDeleting = false,
                            error = result.message
                        )
                    }
                    _sideEffects.send(ProfileUiSideEffect.ShowToast("Erro: ${result.message}"))
                }
            }
        }
    }
}