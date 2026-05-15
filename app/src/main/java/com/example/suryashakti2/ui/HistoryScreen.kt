package com.example.suryashakti2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.suryashakti2.viewmodel.EnergyViewModel

@Composable
fun HistoryScreen(viewModel: EnergyViewModel) {

    val logs by viewModel.allLogs.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "📜 History",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (logs.isEmpty()) {
            Text("No data available yet")
        } else {
            LazyColumn {

                items(logs) { log ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text("📅 ${log.date}")

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("🔆 Generation: ${"%.2f".format(log.generation)} kWh")
                            Text("🏠 Consumption: ${"%.2f".format(log.consumption)} kWh")
                            Text("💰 Savings: ₹${"%.2f".format(log.savings)}")

                        }
                    }
                }
            }
        }
    }
}