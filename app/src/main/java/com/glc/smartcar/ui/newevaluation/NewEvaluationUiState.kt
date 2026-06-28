package com.glc.smartcar.ui.newevaluation

import com.glc.smartcar.data.model.fipe.FipeItemResponse

data class NewEvaluationUiState(
    val brands: List<FipeItemResponse> = emptyList(),
    val models: List<FipeItemResponse> = emptyList(),
    val years: List<FipeItemResponse> = emptyList(),
    val selectedBrand: FipeItemResponse? = null,
    val selectedModel: FipeItemResponse? = null,
    val selectedYear: FipeItemResponse? = null,
    val mileage: String = "",
    val condition: String = "",
    val additionalNotes: String = "",
    val price: String = "",
    val isLoadingBrands: Boolean = false,
    val isLoadingModels: Boolean = false,
    val isLoadingYears: Boolean = false,
    val isSubmitEnabled: Boolean = false,
    val isCalculating: Boolean = false,
    val error: String? = null
)
