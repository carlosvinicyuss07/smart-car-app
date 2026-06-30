package com.glc.smartcar.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glc.smartcar.core.Result
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse
import com.glc.smartcar.data.repository.AvaliacaoRepositoryInterface
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: AvaliacaoRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<HistoryUiSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.OnSearchQueryChanged -> {
                _uiState.update { state ->
                    val query = event.query
                    state.copy(
                        searchQuery = query,
                        filteredAvaliacoes = filterAvaliacoes(state.avaliacoes, query)
                    )
                }
            }
            is HistoryUiEvent.OnCarregarAvaliacoes -> {
                carregarAvaliacoes()
            }
            is HistoryUiEvent.OnAvaliacaoClick -> {
                viewModelScope.launch {
                    _sideEffects.send(HistoryUiSideEffect.NavigateToDetails(event.id))
                }
            }
        }
    }

    private fun carregarAvaliacoes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.listarAvaliacoes()) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            avaliacoes = result.data,
                            filteredAvaliacoes = filterAvaliacoes(result.data, state.searchQuery)
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                    _sideEffects.send(HistoryUiSideEffect.ShowToast(result.message))
                }
            }
        }
    }

    private fun filterAvaliacoes(list: List<AvaliacaoResponse>, query: String): List<AvaliacaoResponse> {
        if (query.isBlank()) return list
        return list.filter { item ->
            val carName = if (item.veiculo != null) {
                "${item.veiculo.marca} ${item.veiculo.modelo} ${item.veiculo.ano}"
            } else {
                item.fipeId
            }
            item.fipeId.contains(query, ignoreCase = true) ||
            item.statusResultado.contains(query, ignoreCase = true) ||
            item.notasPessoais.contains(query, ignoreCase = true) ||
            carName.contains(query, ignoreCase = true)
        }
    }
}
