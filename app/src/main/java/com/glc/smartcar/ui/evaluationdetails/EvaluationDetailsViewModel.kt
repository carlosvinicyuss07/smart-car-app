package com.glc.smartcar.ui.evaluationdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glc.smartcar.core.Result
import com.glc.smartcar.data.repository.AvaliacaoRepositoryInterface
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvaluationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AvaliacaoRepositoryInterface
) : ViewModel() {

    private val avaliacaoId: Long = checkNotNull(savedStateHandle["avaliacaoId"])

    private val _uiState = MutableStateFlow(EvaluationDetailsUiState())
    val uiState: StateFlow<EvaluationDetailsUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<EvaluationDetailsUiSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        carregarAvaliacao()
    }

    fun onEvent(event: EvaluationDetailsUiEvent) {
        when (event) {
            is EvaluationDetailsUiEvent.OnDeleteClick -> {
                _uiState.update { it.copy(showDeleteDialog = true) }
            }
            is EvaluationDetailsUiEvent.OnConfirmDelete -> {
                _uiState.update { it.copy(showDeleteDialog = false) }
                excluirAvaliacao()
            }
            is EvaluationDetailsUiEvent.OnDismissDeleteDialog -> {
                _uiState.update { it.copy(showDeleteDialog = false) }
            }
            is EvaluationDetailsUiEvent.OnBackClick -> {
                viewModelScope.launch {
                    _sideEffects.send(EvaluationDetailsUiSideEffect.NavigateBack)
                }
            }
        }
    }

    private fun carregarAvaliacao() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.buscarAvaliacao(avaliacaoId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, avaliacao = result.data) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                    _sideEffects.send(EvaluationDetailsUiSideEffect.ShowToast(result.message))
                }
            }
        }
    }

    private fun excluirAvaliacao() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            when (val result = repository.desativarAvaliacao(avaliacaoId)) {
                is Result.Success -> {
                    _sideEffects.send(
                        EvaluationDetailsUiSideEffect.ShowToast("Avaliação excluída com sucesso!")
                    )
                    _sideEffects.send(EvaluationDetailsUiSideEffect.NavigateBack)
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isDeleting = false) }
                    _sideEffects.send(EvaluationDetailsUiSideEffect.ShowToast(result.message))
                }
            }
        }
    }
}
