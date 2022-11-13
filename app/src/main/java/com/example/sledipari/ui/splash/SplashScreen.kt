package com.example.sledipari.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sledipari.R

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: GetAllMonthsViewModel
) {

    val isLoading by viewModel.loading.collectAsState()
    val completed by viewModel.completed.collectAsState()

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {

        LaunchedEffect(key1 = true) {
            viewModel.restoreAllMonths()
        }

        LaunchedEffect(key1 = completed) {
            if (viewModel.completed.value) {
                navController.navigate("main_screen") {
                    popUpTo("splash_screen") {
                        inclusive = true
                    }
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 250.dp)
            )
        }

    }

}