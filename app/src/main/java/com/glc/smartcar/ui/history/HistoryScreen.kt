package com.glc.smartcar.ui.history

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse
import com.glc.smartcar.ui.components.cards.EvaluationStatus
import com.glc.smartcar.ui.components.cards.HistoryCardComponent
import com.glc.smartcar.ui.components.inputs.SearchInputComponent
import com.glc.smartcar.ui.components.topbar.TopBarComponent
import com.glc.smartcar.ui.theme.SmartCarTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.Locale

import androidx.compose.ui.input.nestedscroll.nestedScroll

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = koinViewModel(),
    onNavigateToDetails: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(HistoryUiEvent.OnCarregarAvaliacoes)
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is HistoryUiSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is HistoryUiSideEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.id)
                }
                HistoryUiSideEffect.NavigateToNewEvaluation -> {
                    Toast.makeText(context, "Abrir Nova Avaliação", Toast.LENGTH_SHORT).show()
                }
                HistoryUiSideEffect.NavigateToProfile -> {
                    Toast.makeText(context, "Abrir Perfil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    HistoryScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    state: HistoryUiState,
    onEvent: (HistoryUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                TopBarComponent(
                    title = "Meu histórico"
                )
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    SearchInputComponent(
                        value = state.searchQuery,
                        onValueChange = { onEvent(HistoryUiEvent.OnSearchQueryChanged(it)) }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        androidx.compose.material3.pulltorefresh.PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { onEvent(HistoryUiEvent.OnCarregarAvaliacoes) },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.error != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Não foi possível carregar o histórico",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onEvent(HistoryUiEvent.OnCarregarAvaliacoes) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Outlined.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Tentar novamente")
                    }
                }
            } else if (!state.isLoading && state.filteredAvaliacoes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (state.searchQuery.isNotEmpty()) "Nenhuma avaliação encontrada" else "Seu histórico está vazio",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (state.searchQuery.isNotEmpty()) "Tente buscar por outro termo ou modelo." else "Realize uma nova avaliação para vê-la listada aqui.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    items(state.filteredAvaliacoes, key = { it.id }) { avaliacao ->
                        val carName = if (avaliacao.veiculo != null) {
                            "${avaliacao.veiculo.marca} ${avaliacao.veiculo.modelo} (${avaliacao.veiculo.ano})"
                        } else {
                            avaliacao.fipeId
                        }
                        val priceStr = formatCurrency(avaliacao.precoDesejado)
                        val dateStr = formatDate(avaliacao.criadoA)
                        val statusEnum = mapStatus(avaliacao.statusResultado)

                        HistoryCardComponent(
                            carModel = carName,
                            price = priceStr,
                            date = dateStr,
                            status = statusEnum,
                            modifier = Modifier.clickable {
                                onEvent(HistoryUiEvent.OnAvaliacaoClick(avaliacao.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun formatCurrency(value: Double): String {
    return try {
        val format = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("pt").setRegion("BR").build())
        format.format(value)
    } catch (e: Exception) {
        "R$ $value"
    }
}

private fun formatDate(dateStr: String): String {
    if (dateStr.length == 10 && dateStr[2] == '-' && dateStr[5] == '-') return dateStr
    if (dateStr.length == 10 && dateStr[2] == '/' && dateStr[5] == '/') return dateStr
    
    return try {
        if (dateStr.contains("T")) {
            val parts = dateStr.split("T")[0].split("-")
            if (parts.size == 3) {
                "${parts[2]}-${parts[1]}-${parts[0]}"
            } else {
                dateStr
            }
        } else {
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}

private fun mapStatus(status: String): EvaluationStatus {
    return when (status.uppercase()) {
        "GOOD_DEAL", "BOM_NEGOCIO", "ÓTIMO NEGÓCIO", "OTIMO_NEGOCIO" -> EvaluationStatus.EXCELLENT_DEAL
        "BAD_DEAL", "PÉSSIMO NEGÓCIO", "PESSIMO_NEGOCIO" -> EvaluationStatus.BAD_DEAL
        "AVERAGE", "NA MÉDIA", "NA_MEDIA" -> EvaluationStatus.AVERAGE
        "ABOVE_FIPE", "ACIMA DA FIPE", "ACIMA_FIPE" -> EvaluationStatus.ABOVE_FIPE
        else -> EvaluationStatus.AVERAGE
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
private fun HistoryScreenPreview() {
    val mockAvaliacoes = listOf(
        AvaliacaoResponse(
            id = 1L,
            fipeId = "001234-5",
            precoDesejado = 138500.0,
            precoFipe = 142000.0,
            variacao = -2.46,
            statusResultado = "BOM_NEGOCIO",
            criadoA = "24-10-2023",
            conservacao = "Excelente",
            historicoAtivo = "true",
            notasPessoais = "Único dono, sem detalhes."
        ),
        AvaliacaoResponse(
            id = 2L,
            fipeId = "002345-6",
            precoDesejado = 152000.0,
            precoFipe = 145000.0,
            variacao = 4.82,
            statusResultado = "PESSIMO_NEGOCIO",
            criadoA = "24-10-2023",
            conservacao = "Bom",
            historicoAtivo = "true",
            notasPessoais = "Pneus gastos, batido na traseira."
        ),
        AvaliacaoResponse(
            id = 3L,
            fipeId = "003456-7",
            precoDesejado = 165900.0,
            precoFipe = 165000.0,
            variacao = 0.54,
            statusResultado = "NA_MEDIA",
            criadoA = "19-10-2023",
            conservacao = "Excelente",
            historicoAtivo = "true",
            notasPessoais = ""
        )
    )

    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            HistoryScreenContent(
                state = HistoryUiState(
                    avaliacoes = mockAvaliacoes,
                    filteredAvaliacoes = mockAvaliacoes
                ),
                onEvent = {}
            )
        }
    }
}
