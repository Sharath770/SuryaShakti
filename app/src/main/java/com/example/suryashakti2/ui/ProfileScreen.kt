package com.example.suryashakti2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.suryashakti2.data.UserProfile

@Composable
fun ProfileScreen(
    profile: UserProfile?,
    onSave: (Double, Double, Double) -> Unit
) {
    var capacity by remember { mutableStateOf(profile?.panelCapacity?.toString() ?: "") }
    var rate by remember { mutableStateOf(profile?.gridRate?.toString() ?: "") }
    var tariff by remember { mutableStateOf(profile?.feedInTariff?.toString() ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Edit Profile", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(capacity, { capacity = it }, label = { Text("Panel Capacity") })
        OutlinedTextField(rate, { rate = it }, label = { Text("Grid Rate") })
        OutlinedTextField(tariff, { tariff = it }, label = { Text("Feed-in Tariff") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onSave(
                capacity.toDoubleOrNull() ?: 0.0,
                rate.toDoubleOrNull() ?: 0.0,
                tariff.toDoubleOrNull() ?: 0.0
            )
        }) {
            Text("Update Profile")
        }
    }
}