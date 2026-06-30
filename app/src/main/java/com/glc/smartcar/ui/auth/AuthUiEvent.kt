package com.glc.smartcar.ui.auth

sealed class AuthUiEvent {
    data class OnNomeChanged(val value: String) : AuthUiEvent()
    data class OnEmailChanged(val value: String) : AuthUiEvent()
    data class OnSenhaChanged(val value: String) : AuthUiEvent()
    data class OnConfirmarSenhaChanged(val value: String) : AuthUiEvent()

    object OnToggleModeClick : AuthUiEvent()
    object OnSubmitClick : AuthUiEvent()
}