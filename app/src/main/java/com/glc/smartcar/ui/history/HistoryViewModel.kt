package com.glc.smartcar.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glc.smartcar.core.Result
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse
import com.glc.smartcar.data.repository.AvaliacaoRepositoryInterface
import com.glc.smartcar.ui.components.navigation.BottomNavItem
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

    init {
        carregarAvaliacoes()
    }

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
            is HistoryUiEvent.OnBottomTabSelected -> {
                viewModelScope.launch {
                    when (event.tab) {
                        BottomNavItem.HISTORY -> {}
                        BottomNavItem.NEW_EVALUATION -> {
                            _sideEffects.send(HistoryUiSideEffect.NavigateToNewEvaluation)
                        }
                        BottomNavItem.PROFILE -> {
                            _sideEffects.send(HistoryUiSideEffect.NavigateToProfile)
                        }
                    }
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
            item.fipeId.contains(query, ignoreCase = true) ||
            item.statusResultado.contains(query, ignoreCase = true) ||
            item.notasPessoais.contains(query, ignoreCase = true) ||
            getCarNameFallback(item.fipeId).contains(query, ignoreCase = true)
        }
    }

    private fun getCarNameFallback(fipeId: String): String {
        return when (fipeId) {
            "001234-5" -> "Toyota Corolla XEi 2.0"
            "002345-6" -> "Honda Civic Touring 1.5"
            "003456-7" -> "Volkswagen T-Cross Highline"
            "004567-8" -> "Jeep Compass Longitude"
            else -> ""
        }
    }
}
