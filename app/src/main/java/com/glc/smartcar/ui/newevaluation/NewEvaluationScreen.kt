package com.glc.smartcar.ui.newevaluation

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glc.smartcar.ui.components.buttons.ExtendedButtonWithIconComponent
import com.glc.smartcar.ui.components.inputs.OutlinedInputWithIconComponent
import com.glc.smartcar.ui.components.inputs.PriceInputComponent
import com.glc.smartcar.ui.components.text.SectionTitleComponent
import com.glc.smartcar.ui.components.topbar.TopBarComponent
import com.glc.smartcar.ui.theme.SmartCarTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewEvaluationScreen(
    viewModel: NewEvaluationViewModel = koinViewModel(),
    onNavigateToDetails: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is NewEvaluationUiSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                NewEvaluationUiSideEffect.NavigateToDetails -> {
                    onNavigateToDetails()
                }
            }
        }
    }

    NewEvaluationContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun NewEvaluationContent(
    state: NewEvaluationUiState,
    onEvent: (NewEvaluationUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    var brandMenuExpanded by remember { mutableStateOf(false) }
    var modelMenuExpanded by remember { mutableStateOf(false) }
    var yearMenuExpanded by remember { mutableStateOf(false) }
    var conditionMenuExpanded by remember { mutableStateOf(false) }

    val conditions = listOf("Excelente", "Bom", "Na Média", "Ruim", "Péssimo")

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                TopBarComponent(
                    title = "Nova avaliação"
                )
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {

            SectionTitleComponent(title = "Selecione o veículo")
            Spacer(modifier = Modifier.height(16.dp))

            Box {
                OutlinedInputWithIconComponent(
                    label = "Marca",
                    placeholder = if (state.isLoadingBrands) "Carregando..." else "Selecione a marca",
                    icon = Icons.Outlined.DirectionsCar,
                    value = state.selectedBrand?.name ?: "",
                    onValueChange = {},
                    trailingIcon = Icons.Outlined.KeyboardArrowDown,
                    isDropdown = true,
                    onClick = { if (!state.isLoadingBrands && state.brands.isNotEmpty()) brandMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = brandMenuExpanded,
                    onDismissRequest = { brandMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    state.brands.forEach { brand ->
                        DropdownMenuItem(
                            text = { Text(text = brand.name) },
                            onClick = {
                                onEvent(NewEvaluationUiEvent.OnBrandSelected(brand))
                                brandMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box {
                OutlinedInputWithIconComponent(
                    label = "Modelo",
                    placeholder = if (state.isLoadingModels) "Carregando..." else "Selecione o modelo",
                    icon = Icons.Outlined.DirectionsCar,
                    value = state.selectedModel?.name ?: "",
                    onValueChange = {},
                    trailingIcon = Icons.Outlined.KeyboardArrowDown,
                    isDropdown = true,
                    onClick = { if (!state.isLoadingModels && state.models.isNotEmpty()) modelMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = modelMenuExpanded,
                    onDismissRequest = { modelMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    state.models.forEach { model ->
                        DropdownMenuItem(
                            text = { Text(text = model.name) },
                            onClick = {
                                onEvent(NewEvaluationUiEvent.OnModelSelected(model))
                                modelMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Box {
                OutlinedInputWithIconComponent(
                    label = "Ano",
                    placeholder = if (state.isLoadingYears) "Carregando..." else "Selecione o ano",
                    icon = Icons.Outlined.CalendarMonth,
                    value = state.selectedYear?.name ?: "",
                    onValueChange = {},
                    trailingIcon = Icons.Outlined.KeyboardArrowDown,
                    isDropdown = true,
                    onClick = { if (!state.isLoadingYears && state.years.isNotEmpty()) yearMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = yearMenuExpanded,
                    onDismissRequest = { yearMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    state.years.forEach { year ->
                        DropdownMenuItem(
                            text = { Text(text = year.name) },
                            onClick = {
                                onEvent(NewEvaluationUiEvent.OnYearSelected(year))
                                yearMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionTitleComponent(title = "Informações")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInputWithIconComponent(
                label = "Quilometragem",
                placeholder = "Digite a quilometragem atual",
                icon = Icons.Outlined.Speed,
                value = state.mileage,
                onValueChange = { onEvent(NewEvaluationUiEvent.OnMileageChanged(it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box {
                OutlinedInputWithIconComponent(
                    label = "Conservação",
                    placeholder = "Selecione o estado do carro",
                    icon = Icons.Outlined.Security,
                    value = state.condition,
                    onValueChange = {},
                    trailingIcon = Icons.Outlined.KeyboardArrowDown,
                    isDropdown = true,
                    onClick = { conditionMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = conditionMenuExpanded,
                    onDismissRequest = { conditionMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    conditions.forEach { condition ->
                        DropdownMenuItem(
                            text = { Text(text = condition) },
                            onClick = {
                                onEvent(NewEvaluationUiEvent.OnConditionSelected(condition))
                                conditionMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedInputWithIconComponent(
                label = "Notas adicionais",
                placeholder = "Digite informações importantes",
                icon = Icons.Outlined.Info,
                value = state.additionalNotes,
                onValueChange = { onEvent(NewEvaluationUiEvent.OnNotesChanged(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            PriceInputComponent(
                value = state.price,
                onValueChange = { onEvent(NewEvaluationUiEvent.OnPriceChanged(it)) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isCalculating) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    ExtendedButtonWithIconComponent(
                        text = "Calcular Avaliação",
                        icon = Icons.Outlined.Calculate,
                        onClick = { onEvent(NewEvaluationUiEvent.OnCalculateClick) },
                        enabled = state.isSubmitEnabled
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
private fun NewEvaluationScreenPreview() {
    SmartCarTheme {
        NewEvaluationContent(
            state = NewEvaluationUiState(),
            onEvent = {}
        )
    }
}
