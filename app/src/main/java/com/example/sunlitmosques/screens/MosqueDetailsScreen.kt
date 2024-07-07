package com.example.sunlitmosques.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sunlitmosques.viewmodels.MosqueViewModel


@Composable
fun MosqueDetailsScreen(mosqueViewModel: MosqueViewModel = viewModel()) {
    val selectedMosque = mosqueViewModel.selectedMosque.collectAsState()

    selectedMosque.value?.let { mosque ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Account Number: ${mosque.accountNumber}",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Ministry Name: ${mosque.minName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Total Outstanding: ${mosque.totalOutstanding}",
                style = MaterialTheme.typography.bodyLarge
            )
            mosque.monthlyConsumption.forEach { (month, consumption) ->
                Text(text = "$month: $consumption", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}