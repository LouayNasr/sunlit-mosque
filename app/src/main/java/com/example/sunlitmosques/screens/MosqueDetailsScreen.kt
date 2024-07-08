package com.example.sunlitmosques.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sunlitmosques.viewmodels.MosqueViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosqueDetailsScreen(
    navController: NavController,
    mosqueViewModel: MosqueViewModel = viewModel()
) {
    val selectedMosque = mosqueViewModel.selectedMosque.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mosque Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { mosqueViewModel.performCalculation() }) {
                        Icon(Icons.Default.Build, contentDescription = "Perform Calculation")
                    }
                    IconButton(onClick = { mosqueViewModel.performCalculation() }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Navigate to settings screen"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            selectedMosque.value?.let { mosque ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
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
                        Text(
                            text = "$month: $consumption",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } ?: run {
                // Display a loading or error message if no mosque is selected
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = "No mosque selected")
                }
            }
        }
    )
}
