package com.glc.smartcar.ui.history

import com.glc.smartcar.ui.components.navigation.BottomNavItem

sealed class HistoryUiEvent {
    data class OnSearchQueryChanged(val query: String) : HistoryUiEvent()
    object OnCarregarAvaliacoes : HistoryUiEvent()
    data class OnAvaliacaoClick(val id: Long) : HistoryUiEvent()
    data class OnBottomTabSelected(val tab: BottomNavItem) : HistoryUiEvent()
}
