package com.glc.smartcar.ui.evaluationdetails

sealed class EvaluationDetailsUiEvent {
    data object OnDeleteClick : EvaluationDetailsUiEvent()
    data object OnConfirmDelete : EvaluationDetailsUiEvent()
    data object OnDismissDeleteDialog : EvaluationDetailsUiEvent()
    data object OnBackClick : EvaluationDetailsUiEvent()
}
