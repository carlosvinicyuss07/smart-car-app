package com.glc.smartcar.ui.profile

sealed class ProfileUiEvent {
    data object OnLogoutClick : ProfileUiEvent()
    data object OnDeleteAccountClick : ProfileUiEvent()
    data object OnConfirmDeleteAccountClick : ProfileUiEvent()
    data object OnDismissDeleteDialogClick : ProfileUiEvent()
}