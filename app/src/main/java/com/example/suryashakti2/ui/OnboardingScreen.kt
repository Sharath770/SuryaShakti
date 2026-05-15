package com.example.suryashakti2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(
    onSave: (Double, Double, Double) -> Unit
) {

    var capacity by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var tariff by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "⚡ Setup Your Solar Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Panel Capacity (kW)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Grid Rate (₹/unit)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = tariff,
            onValueChange = { tariff = it },
            label = { Text("Feed-in Tariff (₹/unit)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onSave(
                    capacity.toDoubleOrNull() ?: 0.0,
                    rate.toDoubleOrNull() ?: 0.0,
                    tariff.toDoubleOrNull() ?: 0.0
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Save Profile")
        }
    }
}