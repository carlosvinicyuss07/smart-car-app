package com.glc.smartcar.ui.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val nome: String = "",
    val email: String = "",
    val dataCriacao: String = "",
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false
) {
    val membroDesde: String
        get() = "Membro desde: ${dataCriacao.take(4)}"
}
