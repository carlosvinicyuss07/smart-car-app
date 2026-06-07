package com.glc.smartcar.ui.auth

sealed class AuthUiSideEffect {
    data class ShowSnackbar(val message: String) : AuthUiSideEffect()
     object NavigateToHome : AuthUiSideEffect()
}