package com.glc.smartcar.ui.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glc.smartcar.ui.theme.SmartCarTheme

@Composable
fun MarketAnalysisCardComponent(
    statusText: String,
    statusColor: Color,
    statusBgColor: Color,
    statusIcon: ImageVector,
    insertedPrice: String,
    priceDifference: String,
    priceDifferenceColor: Color,
    fipePrice: String,
    variation: String? = null,
    variationColor: Color? = null,
    evaluationDate: String,
    thermometerPosition: Float, // 0f to 1f
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp, horizontal = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier
                    .background(statusBgColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statusText.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = statusColor
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Seu preço inserido",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = insertedPrice,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = priceDifference,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = priceDifferenceColor
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Info Rows
            InfoRow(label = "Mercado (FIPE)", value = fipePrice)
            if (variation != null) {
                Spacer(modifier = Modifier.height(16.dp))
                InfoRow(
                    label = "Variação",
                    value = variation,
                    valueColor = variationColor ?: MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(label = "Avaliado em", value = evaluationDate)
            
            Spacer(modifier = Modifier.height(32.dp))

            MarketThermometer(position = thermometerPosition)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onBackground) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}

@Composable
private fun MarketThermometer(position: Float) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(20.dp)) {
            val width = size.width
            val yCenter = 10.dp.toPx()
            val barHeight = 4.dp.toPx()
            val cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            
            val brush = Brush.horizontalGradient(
                0.0f to Color(0xFF4CAF50), // Green (Abaixo)
                0.5f to Color(0xFFFFC107), // Yellow (Justo)
                1.0f to Color(0xFFF44336)  // Red (Acima)
            )
            
            drawRoundRect(
                brush = brush,
                topLeft = Offset(0f, yCenter - barHeight / 2),
                size = Size(width, barHeight),
                cornerRadius = cornerRadius
            )
            
            val safePos = position.coerceIn(0f, 1f)
            val markerX = width * safePos
            
            drawCircle(
                color = Color.White,
                radius = 7.dp.toPx(),
                center = Offset(markerX, yCenter),
                style = Fill
            )
            drawCircle(
                color = Color.Black,
                radius = 3.dp.toPx(),
                center = Offset(markerX, yCenter),
                style = Fill
            )
            drawCircle(
                color = Color.LightGray,
                radius = 7.dp.toPx(),
                center = Offset(markerX, yCenter),
                style = Stroke(width = 1.dp.toPx())
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ABAIXO",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = "JUSTO",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = "ACIMA",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.outline
            )
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
private fun MarketAnalysisCardComponentPreview() {
    val greenColor = Color(0xFF2E7D32)
    val lightGreenBg = Color(0xFFE8F5E9)

    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MarketAnalysisCardComponent(
                statusText = "Bom negócio",
                statusColor = greenColor,
                statusBgColor = lightGreenBg,
                statusIcon = Icons.Outlined.CheckCircle,
                insertedPrice = "R$ 302.450",
                priceDifference = "R$ 13.450 abaixo do valor de mercado",
                priceDifferenceColor = greenColor,
                fipePrice = "R$ 315.900",
                evaluationDate = "Oct 24, 2023",
                thermometerPosition = 0f,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
