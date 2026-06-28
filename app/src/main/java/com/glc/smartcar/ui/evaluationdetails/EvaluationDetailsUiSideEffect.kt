package com.glc.smartcar.ui.evaluationdetails

sealed class EvaluationDetailsUiSideEffect {
    data class ShowToast(val message: String) : EvaluationDetailsUiSideEffect()
    data object NavigateBack : EvaluationDetailsUiSideEffect()
}
