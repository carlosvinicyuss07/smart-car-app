package com.glc.smartcar.ui.newevaluation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glc.smartcar.core.Result
import com.glc.smartcar.data.model.avaliacao.AvaliacaoRequest
import com.glc.smartcar.data.model.fipe.FipeItemResponse
import com.glc.smartcar.data.repository.AvaliacaoRepositoryInterface
import com.glc.smartcar.data.repository.FipeRepositoryInterface
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class NewEvaluationViewModel(
    private val fipeRepository: FipeRepositoryInterface,
    private val avaliacaoRepository: AvaliacaoRepositoryInterface
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewEvaluationUiState())
    val uiState: StateFlow<NewEvaluationUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<NewEvaluationUiSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        loadBrands()
    }

    fun onEvent(event: NewEvaluationUiEvent) {
        when (event) {
            is NewEvaluationUiEvent.OnBrandSelected -> {
                updateState {
                    it.copy(
                        selectedBrand = event.brand,
                        selectedModel = null,
                        selectedYear = null,
                        models = emptyList(),
                        years = emptyList()
                    ) 
                }
                loadModels(event.brand.code)
            }
            is NewEvaluationUiEvent.OnModelSelected -> {
                updateState {
                    it.copy(
                        selectedModel = event.model,
                        selectedYear = null,
                        years = emptyList()
                    ) 
                }
                val brandId = _uiState.value.selectedBrand?.code ?: return
                loadYears(brandId, event.model.code)
            }
            is NewEvaluationUiEvent.OnYearSelected -> {
                updateState { it.copy(selectedYear = event.year) }
            }
            is NewEvaluationUiEvent.OnMileageChanged -> {
                updateState { it.copy(mileage = event.mileage) }
            }
            is NewEvaluationUiEvent.OnConditionSelected -> {
                updateState { it.copy(condition = event.condition) }
            }
            is NewEvaluationUiEvent.OnNotesChanged -> {
                updateState { it.copy(additionalNotes = event.notes) }
            }
            is NewEvaluationUiEvent.OnPriceChanged -> {
                val cleanString = event.price.replace(Regex("[^0-9]"), "")
                if (cleanString.isEmpty()) {
                    updateState { it.copy(price = "") }
                } else {
                    val parsed = cleanString.toDoubleOrNull() ?: 0.0
                    val formatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.Builder().setLanguage("pt").setRegion("BR").build())
                    formatter.minimumFractionDigits = 2
                    formatter.maximumFractionDigits = 2
                    val formatted = formatter.format(parsed / 100)
                    updateState { it.copy(price = formatted) }
                }
            }
            is NewEvaluationUiEvent.OnCalculateClick -> {
                submitEvaluation()
            }
        }
    }

    private fun updateState(transform: (NewEvaluationUiState) -> NewEvaluationUiState) {
        _uiState.update { currentState ->
            val newState = transform(currentState)
            newState.copy(isSubmitEnabled = checkSubmitEnabled(newState))
        }
    }

    private fun checkSubmitEnabled(state: NewEvaluationUiState): Boolean {
        val priceNotZero = state.price.isNotBlank() && state.price != "0,00"

        return state.selectedBrand != null &&
                state.selectedModel != null &&
                state.selectedYear != null &&
                state.mileage.isNotBlank() &&
                state.condition.isNotBlank() &&
                priceNotZero
    }

    private fun loadBrands() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingBrands = true, error = null) }
            when (val result = fipeRepository.listarMarcas()) {
                is Result.Success -> {
                    _uiState.update { it.copy(brands = result.data, isLoadingBrands = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoadingBrands = false) }
                    _sideEffects.send(NewEvaluationUiSideEffect.ShowToast("Erro ao carregar marcas: ${result.message}"))
                }
            }
        }
    }

    private fun loadModels(brandId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingModels = true, error = null) }
            when (val result = fipeRepository.listarModelos(brandId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(models = result.data, isLoadingModels = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoadingModels = false) }
                    _sideEffects.send(NewEvaluationUiSideEffect.ShowToast("Erro ao carregar modelos: ${result.message}"))
                }
            }
        }
    }

    private fun loadYears(brandId: String, modelId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingYears = true, error = null) }
            when (val result = fipeRepository.listarAnos(brandId, modelId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(years = result.data, isLoadingYears = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(error = result.message, isLoadingYears = false) }
                    _sideEffects.send(NewEvaluationUiSideEffect.ShowToast("Erro ao carregar anos: ${result.message}"))
                }
            }
        }
    }

    private fun submitEvaluation() {
        val state = _uiState.value
        if (!state.isSubmitEnabled || state.isCalculating) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCalculating = true) }

            // Reconverte preço para Double
            val cleanPrice = state.price.replace(Regex("[^0-9]"), "")
            val priceDouble = (cleanPrice.toDoubleOrNull() ?: 0.0) / 100

            val request = AvaliacaoRequest(
                brandId = state.selectedBrand?.code!!,
                modelId = state.selectedModel?.code!!,
                yearId = state.selectedYear?.code!!,
                kmsRodados = state.mileage.toDoubleOrNull() ?: 0.0,
                conservacao = state.condition.uppercase(),
                precoDesejado = priceDouble,
                notasPessoais = state.additionalNotes
            )

            when (val result = avaliacaoRepository.criarAvaliacao(request)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isCalculating = false,
                            selectedBrand = null,
                            selectedModel = null,
                            selectedYear = null,
                            models = emptyList(),
                            years = emptyList(),
                            mileage = "",
                            condition = "",
                            price = "",
                            additionalNotes = "",
                            isSubmitEnabled = false
                        )
                    }
                    _sideEffects.send(
                        NewEvaluationUiSideEffect.ShowToast("Avaliação criada com sucesso!")
                    )
                    _sideEffects.send(NewEvaluationUiSideEffect.NavigateToDetails(result.data.id))
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isCalculating = false, error = result.message) }
                    _sideEffects.send(NewEvaluationUiSideEffect.ShowToast("Erro ao criar avaliação: ${result.message}"))
                }
            }
        }
    }
}
