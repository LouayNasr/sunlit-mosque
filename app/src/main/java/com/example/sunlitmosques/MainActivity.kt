package com.example.sunlitmosques

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.sunlitmosques.ui.theme.SunlitMosquesTheme
import com.example.sunlitmosques.viewmodels.MosqueViewModel
import com.example.sunlitmosques.viewmodels.MosqueViewModelFactory

class MainActivity : ComponentActivity() {

    private val mosqueViewModel: MosqueViewModel by viewModels {
        MosqueViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load mosques from assets
        mosqueViewModel.loadMosquesFromAssets("mosque_data.json")

        setContent {
            SunlitMosquesTheme {
                AppNavigation(mosqueViewModel)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SunlitMosquesTheme {
//        Greeting("Android")
//    }
//}