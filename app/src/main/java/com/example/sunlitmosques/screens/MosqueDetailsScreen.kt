package com.example.sunlitmosques.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import com.example.sunlitmosques.Screen
import com.example.sunlitmosques.viewmodels.MosqueViewModel
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MosqueDetailsScreen(
    navController: NavController,
    mosqueViewModel: MosqueViewModel = viewModel()
) {
    val selectedMosque = mosqueViewModel.selectedMosque.collectAsState()

    Scaffold(
        topBar = {
            BackHandler {
                mosqueViewModel.resetCalculatedValues()
                navController.popBackStack()
            }
            TopAppBar(
                title = { Text("Mosque Details") },
                navigationIcon = {
                    IconButton(onClick = {
                        mosqueViewModel.resetCalculatedValues()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
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
                        text = "Total Outstanding: ${mosque.totalOutstanding}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Monthly Power Consumption:",
                        style = MaterialTheme.typography.titleMedium
                    )

                    BarChartSection(
                        modifier = Modifier
                            .fillMaxWidth(),
                        data = mosque.monthlyConsumption
                    )

                    // Display the calculated values
                    val showWidget by mosqueViewModel.showResultSection.collectAsState()
                    if (showWidget) {
                        ResultSection(mosqueViewModel = mosqueViewModel)
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mosqueViewModel.performCalculation() },
                icon = {},
                text = { Text(text = "Calculate") }
            )
        }
    )
}

@Composable
fun BarChartSection(modifier: Modifier = Modifier, data: Map<String, Double>) {
    val numOfSteps = 5
    val maxValue = data.maxBy { it.value }.value.toInt().div(10).times(11)
    val barDataList = mutableListOf<BarData>()
    data.onEachIndexed { index, (key, value) ->
        barDataList.add(
            BarData(
                point = Point(
                    x = index.toFloat(),
                    y = value.toFloat(),
                ),
                color = Color(
                    Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 96
                ),
                label = key,
                description = "Value of bar $key is value $value" // Simplified description
            )
        )

    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(data.size - 1)
        .bottomPadding(40.dp)
        .axisOffset(20.dp)
        .startDrawPadding(30.dp)
        .axisLabelAngle(20f)
        .labelData { i -> data.entries.toList().get(i).key }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(numOfSteps)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .startDrawPadding(30.dp)
        .labelData { index -> maxValue.div(5).times(index).toString() }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val barChartData = BarChartData(
        chartData = barDataList,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surface
    )
    BarChart(
        modifier
            .height(260.dp)
            .fillMaxWidth(), barChartData = barChartData
    )

}

@Composable
fun ResultSection(mosqueViewModel: MosqueViewModel) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Result:",
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Peak Month: ${mosqueViewModel.peakMonth.collectAsState().value}",
        style = MaterialTheme.typography.bodyLarge
    )

    Text(
        text = "Solar Panels: ${mosqueViewModel.numberOfPanels.collectAsState().value} panels of 300W each",
        style = MaterialTheme.typography.bodyLarge
    )

    Text(
        text = "Batteries: ${mosqueViewModel.numberOfBatteries.collectAsState().value} batteries of 200Ah, 48V each",
        style = MaterialTheme.typography.bodyLarge
    )

    Text(
        text = "Inverter: ${mosqueViewModel.inverterCapacity.collectAsState().value} W",
        style = MaterialTheme.typography.bodyLarge
    )

    Text(
        text = "Percentage of daily coverage ",
        style = MaterialTheme.typography.headlineMedium
    )
    Slider(
        value = mosqueViewModel.percentageOfDailyCoverage.collectAsState().value,
        onValueChange = { mosqueViewModel.updatePercentageOfDailyCoverage(it) },
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.secondary,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        steps = 20,
        valueRange = 0f..1f
    )
}