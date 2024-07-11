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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.sunlitmosques.MainActivity
import com.example.sunlitmosques.R
import com.example.sunlitmosques.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    val context = LocalContext.current as MainActivity
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        // Enable full-screen mode
        WindowCompat.setDecorFitsSystemWindows(context.window, false)
        WindowInsetsControllerCompat(context.window, context.window.decorView).let { controller ->
            controller.hide(android.view.WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        }
        startAnimation = true
        delay(3000)
        navController.navigate(Screen.MosqueList.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
        // Restore the default system bars behavior after navigation
        scope.launch {
            delay(100)  // Ensure navigation has taken place before restoring
            WindowCompat.setDecorFitsSystemWindows(context.window, true)
            WindowInsetsControllerCompat(context.window, context.window.decorView).show(
                android.view.WindowInsets.Type.systemBars()
            )
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