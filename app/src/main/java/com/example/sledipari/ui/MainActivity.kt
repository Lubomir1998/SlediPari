package com.example.sledipari.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sledipari.ui.main.GetMonthViewModel
import com.example.sledipari.ui.main.MonthScreen
import com.example.sledipari.ui.splash.GetAllMonthsViewModel
import com.example.sledipari.utility.Constants.SLEDI_PARI_TOPIC
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().subscribeToTopic(SLEDI_PARI_TOPIC)

        setContent {

            val navController = rememberNavController()
            val getMonthViewModel: GetMonthViewModel = viewModel()
            val getAllMonthsViewModel: GetAllMonthsViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "splash_screen"
            ) {
                composable("splash_screen") {
                    SplashScreen(
                        navController = navController,
                        viewModel = getAllMonthsViewModel
                    )
                }

                composable("main_screen") {
                    MonthScreen(
                        viewModel = getMonthViewModel,
                        activity = this@MainActivity,
                        view = window.decorView.rootView
                    )
                }
            }


        }
    }
}
