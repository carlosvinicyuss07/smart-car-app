package com.glc.smartcar.ui.history

sealed class HistoryUiEvent {
    data class OnSearchQueryChanged(val query: String) : HistoryUiEvent()
    object OnCarregarAvaliacoes : HistoryUiEvent()
    data class OnAvaliacaoClick(val id: Long) : HistoryUiEvent()
}
