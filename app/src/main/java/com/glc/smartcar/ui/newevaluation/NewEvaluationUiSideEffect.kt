package com.glc.smartcar.ui.newevaluation

sealed class NewEvaluationUiSideEffect {
    data class ShowToast(val message: String) : NewEvaluationUiSideEffect()
    data object NavigateToDetails : NewEvaluationUiSideEffect()
}
