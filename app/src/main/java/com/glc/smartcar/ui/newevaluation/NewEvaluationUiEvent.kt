package com.glc.smartcar.ui.newevaluation

import com.glc.smartcar.data.model.fipe.FipeItemResponse

sealed class NewEvaluationUiEvent {
    data class OnBrandSelected(val brand: FipeItemResponse) : NewEvaluationUiEvent()
    data class OnModelSelected(val model: FipeItemResponse) : NewEvaluationUiEvent()
    data class OnYearSelected(val year: FipeItemResponse) : NewEvaluationUiEvent()
    data class OnMileageChanged(val mileage: String) : NewEvaluationUiEvent()
    data class OnConditionSelected(val condition: String) : NewEvaluationUiEvent()
    data class OnNotesChanged(val notes: String) : NewEvaluationUiEvent()
    data class OnPriceChanged(val price: String) : NewEvaluationUiEvent()
    data object OnCalculateClick : NewEvaluationUiEvent()
}
