package com.example.sunlitmosques

import MosqueListScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sunlitmosques.screens.MosqueDetailsScreen
import com.example.sunlitmosques.screens.SplashScreen
import com.example.sunlitmosques.viewmodels.MosqueViewModel


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object MosqueList : Screen("mosqueList")
    object MosqueDetails : Screen("mosqueDetails")
}

@Composable
fun AppNavigation(mosqueViewModel: MosqueViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.MosqueList.route) {
            MosqueListScreen(navController, mosqueViewModel)
        }
        composable(Screen.MosqueDetails.route) {
            MosqueDetailsScreen(navController, mosqueViewModel)
        }
    }
}