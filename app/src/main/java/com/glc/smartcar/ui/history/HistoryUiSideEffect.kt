package com.glc.smartcar.ui.history

sealed class HistoryUiSideEffect {
    data class ShowToast(val message: String) : HistoryUiSideEffect()
    data class NavigateToDetails(val id: Long) : HistoryUiSideEffect()
    object NavigateToNewEvaluation : HistoryUiSideEffect()
    object NavigateToProfile : HistoryUiSideEffect()
}
