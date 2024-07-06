package com.example.sunlitmosques

import JsonParser
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sunlitmosques.models.Mosque
import com.example.sunlitmosques.ui.theme.SunlitMosquesTheme
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Read the JSON file from assets directory
        val jsonString = readJsonFromAssets("mosque_data.json")

        // Parse the JSON data
        val mosqueList = jsonString?.let { JsonParser().parseJsonToMosqueList(it) } ?: emptyList()


        enableEdgeToEdge()
        setContent {
            SunlitMosquesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MosqueListScreen(mosqueList)
                }
            }
        }
    }

    private fun readJsonFromAssets(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
fun MosqueListScreen(mosqueList: List<Mosque>) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        mosqueList.forEach { mosque ->
            MosquePowerConsumptionItem(mosque)
        }
    }
}

@Composable
fun MosquePowerConsumptionItem(mosque: Mosque) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Account Number: ${mosque.accountNumber}")
        Text(text = "Total Outstanding: ${mosque.totalOutstanding}")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SunlitMosquesTheme {
        Greeting("Android")
    }
}