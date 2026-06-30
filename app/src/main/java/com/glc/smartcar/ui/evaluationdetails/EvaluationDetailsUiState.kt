package com.glc.smartcar.ui.evaluationdetails

import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse

data class EvaluationDetailsUiState(
    val isLoading: Boolean = true,
    val avaliacao: AvaliacaoResponse? = null,
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false
)
