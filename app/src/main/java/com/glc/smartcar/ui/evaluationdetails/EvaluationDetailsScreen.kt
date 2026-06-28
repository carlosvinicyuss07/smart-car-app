package com.glc.smartcar.ui.evaluationdetails

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glc.smartcar.data.model.avaliacao.AvaliacaoResponse
import com.glc.smartcar.data.model.avaliacao.VeiculoObject
import com.glc.smartcar.ui.components.cards.ExpertReviewCardComponent
import com.glc.smartcar.ui.components.cards.MarketAnalysisCardComponent
import com.glc.smartcar.ui.components.cards.UserNoteCardComponent
import com.glc.smartcar.ui.components.text.SubSectionTitleComponent
import com.glc.smartcar.ui.components.headers.CarHeaderComponent
import com.glc.smartcar.ui.components.topbar.TopBarComponent
import com.glc.smartcar.ui.theme.SmartCarTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EvaluationDetailsScreen(
    viewModel: EvaluationDetailsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is EvaluationDetailsUiSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is EvaluationDetailsUiSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    EvaluationDetailsScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun EvaluationDetailsScreenContent(
    state: EvaluationDetailsUiState,
    onEvent: (EvaluationDetailsUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                title = "Detalhes",
                onBackClick = { onEvent(EvaluationDetailsUiEvent.OnBackClick) },
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Erro ao carregar detalhes",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onEvent(EvaluationDetailsUiEvent.OnBackClick) }) {
                        Text("Voltar")
                    }
                }
            } else if (state.avaliacao != null) {
                val avaliacao = state.avaliacao
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp)
                ) {
                    val carName = if (avaliacao.veiculo != null) {
                        "${avaliacao.veiculo.marca} ${avaliacao.veiculo.modelo}"
                    } else {
                        avaliacao.fipeId
                    }
                    val carYear = avaliacao.veiculo?.ano?.toString() ?: ""

                    Spacer(modifier = Modifier.height(16.dp))
                    CarHeaderComponent(
                        carModel = carName,
                        carYear = carYear
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    SubSectionTitleComponent(title = "Análise de mercado")
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val priceStr = formatCurrency(avaliacao.precoDesejado)
                    val fipeStr = formatCurrency(avaliacao.precoFipe)
                    val diff = avaliacao.precoDesejado - avaliacao.precoFipe
                    val diffStr = if (diff > 0) "+ ${formatCurrency(diff)}" else formatCurrency(diff)
                    
                    val statusData = mapStatus(avaliacao.statusResultado)
                    
                    val varText = String.format(Locale.US, "%.1f%%", avaliacao.variacao)
                    val variationColor = if (avaliacao.variacao > 0) Color(0xFFC62828) else Color(0xFF2E7D32)

                    MarketAnalysisCardComponent(
                        statusText = statusData.label,
                        statusColor = statusData.color,
                        statusBgColor = statusData.bgColor,
                        statusIcon = statusData.icon,
                        insertedPrice = priceStr,
                        priceDifference = diffStr,
                        priceDifferenceColor = statusData.color,
                        fipePrice = fipeStr,
                        variation = varText,
                        variationColor = variationColor,
                        evaluationDate = formatDate(avaliacao.criadoA),
                        thermometerPosition = statusData.position
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    SubSectionTitleComponent(title = "Suas notas")
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    UserNoteCardComponent(
                        text = avaliacao.notasPessoais.ifEmpty { "Nenhuma nota adicionada." }
                    )
                    
                    if (!avaliacao.avaliacaoIA.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        SubSectionTitleComponent(title = "Análise Especialista (IA)")
                        Spacer(modifier = Modifier.height(12.dp))
                        ExpertReviewCardComponent(
                            title = "Análise da IA",
                            description = avaliacao.avaliacaoIA
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { onEvent(EvaluationDetailsUiEvent.OnDeleteClick) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Excluir relatório",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
        
        if (state.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { onEvent(EvaluationDetailsUiEvent.OnDismissDeleteDialog) },
                title = { Text(text = "Excluir avaliação") },
                text = { Text(text = "Tem certeza que deseja excluir esta avaliação? Esta ação não pode ser desfeita.") },
                confirmButton = {
                    Button(
                        onClick = { onEvent(EvaluationDetailsUiEvent.OnConfirmDelete) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        if (state.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(2.dp),
                                color = MaterialTheme.colorScheme.onError,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Excluir")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(EvaluationDetailsUiEvent.OnDismissDeleteDialog) }) {
                        Text("Cancelar")
                    }
                }
            )
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
    if (dateStr.length >= 10) {
        if (dateStr[2] == '-' || dateStr[2] == '/') return dateStr.substring(0, 10)
    }
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

private data class StatusData(val color: Color, val bgColor: Color, val icon: androidx.compose.ui.graphics.vector.ImageVector, val position: Float, val label: String)

private fun mapStatus(status: String): StatusData {
    val up = status.uppercase()
    return when {
        up.contains("OTIMO") || up.contains("BOM") || up.contains("GOOD") -> {
            StatusData(Color(0xFF2E7D32), Color(0xFFE8F5E9), Icons.Outlined.CheckCircle, 0.15f, "Ótimo negócio")
        }
        up.contains("PESSIMO") || up.contains("BAD") || up.contains("RUIM") -> {
            StatusData(Color(0xFFC62828), Color(0xFFFFEBEE), Icons.Outlined.Warning, 0.85f, "Péssimo negócio")
        }
        up.contains("ACIMA") || up.contains("ABOVE") -> {
            StatusData(Color(0xFFF9A825), Color(0xFFFFF8E1), Icons.Outlined.Info, 0.65f, "Acima da FIPE")
        }
        else -> {
            // AVERAGE / NA_MEDIA
            StatusData(Color(0xFF1565C0), Color(0xFFE3F2FD), Icons.Outlined.CheckCircle, 0.5f, "Na média")
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
private fun EvaluationDetailsScreenPreview() {
    val mockAvaliacao = AvaliacaoResponse(
        id = 1L,
        fipeId = "001015-9",
        veiculo = VeiculoObject(
            marca = "Fiat",
            modelo = "Mobi Trekking 1.0 Flex",
            ano = 2023,
            combustivel = "Flex"
        ),
        precoDesejado = 59000.0,
        precoFipe = 61500.0,
        variacao = -4.06,
        statusResultado = "OTIMO_NEGOCIO",
        criadoA = "2023-11-20T14:30:00",
        conservacao = "NOVO",
        historicoAtivo = "SIM",
        notasPessoais = "Carro de garagem, único dono. IPVA pago.",
        avaliacaoIA = "Este veículo encontra-se aproximadamente 4% abaixo do valor da tabela FIPE. Considerando o ano de 2023, trata-se de uma excelente oportunidade de negócio."
    )

    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EvaluationDetailsScreenContent(
                state = EvaluationDetailsUiState(
                    isLoading = false,
                    avaliacao = mockAvaliacao
                ),
                onEvent = {}
            )
        }
    }
}
