package com.example.suryashakti2.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.Alignment
import com.example.suryashakti2.viewmodel.*

@Composable
fun EnergyDashboardScreen(
    uiState: EnergyUiState,
    onSolarGenerationChanged: (String) -> Unit,
    onStartReadingChanged: (String) -> Unit,
    onEndReadingChanged: (String) -> Unit,
    onRolloverToggle: (Boolean) -> Unit,
    onWeatherSelected: (WeatherCondition) -> Unit,
    onSaveDailyLog: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔹 TITLE
        Text(
            "⚡ SuryaShakti Dashboard",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 INPUTS
        OutlinedTextField(
            value = uiState.solarGenerationInput,
            onValueChange = onSolarGenerationChanged,
            label = { Text("Solar Generation (kWh)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = uiState.startReadingInput,
            onValueChange = onStartReadingChanged,
            label = { Text("Start Reading") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = uiState.endReadingInput,
            onValueChange = onEndReadingChanged,
            label = { Text("End Reading") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 🔹 ROLLOVER
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.isRollover,
                onCheckedChange = onRolloverToggle
            )
            Text("Meter Rollover")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 WEATHER
        Row {
            WeatherCondition.values().forEach {
                Button(
                    onClick = { onWeatherSelected(it) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(it.displayName)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔥 CUSTOM CIRCULAR GRAPH
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Canvas(modifier = Modifier.size(150.dp)) {

                val sweepAngle =
                    ((uiState.independenceScore / 100.0) * 360.0).toFloat()

                // 🔶 Solar (Yellow)
                drawArc(
                    color = Color(0xFFFFD700),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 20f)
                )

                // ⚫ Grid (Black)
                drawArc(
                    color = Color.Black,
                    startAngle = -90f + sweepAngle,
                    sweepAngle = 360f - sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 20f)
                )
            }

            // 🔹 Center text
            Text(
                text = "${uiState.independenceScore.toInt()}%",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 SUMMARY
        Text("💰 Savings: ₹${"%.2f".format(uiState.netSavings)}")
        Text("⚡ Independence: ${uiState.independenceScore.toInt()}%")

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 SAVE BUTTON
        Button(
            onClick = onSaveDailyLog,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}