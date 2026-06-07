package com.glc.smartcar.ui.history

import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse

data class HistoryUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val avaliacoes: List<AvaliacaoResponse> = emptyList(),
    val filteredAvaliacoes: List<AvaliacaoResponse> = emptyList(),
    val error: String? = null
)
