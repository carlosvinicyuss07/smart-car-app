package com.glc.smartcar.ui.profile

sealed class ProfileUiSideEffect {
    data object NavigateToLogin : ProfileUiSideEffect()
    data class ShowToast(val message: String) : ProfileUiSideEffect()
}