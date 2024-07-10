package com.example.sunlitmosques.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sunlitmosques.R
import com.example.sunlitmosques.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        navController.navigate(Screen.MosqueList.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }
    AnimatedSplash(alpha = alphaAnimation.value)
}

@Composable
fun AnimatedSplash(alpha: Float) {
    Box(
        modifier = Modifier
            .background(Color(0xff0c0e12))
            .fillMaxSize()
            .alpha(alpha = alpha),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_splash),
            contentDescription = "splash icon"
        )
    }
}