package com.example.sledipari.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sledipari.ui.info.InfoScreen
import com.example.sledipari.ui.main.GetMonthViewModel
import com.example.sledipari.ui.main.MonthScreen
import com.example.sledipari.ui.settings.SettingsScreen
import com.example.sledipari.ui.settings.history.HistoryScreen
import com.example.sledipari.ui.settings.history.HistoryViewModel
import com.example.sledipari.ui.splash.GetAllMonthsViewModel
import com.example.sledipari.ui.splash.SplashScreen
import com.example.sledipari.utility.Constants.SLEDI_PARI_TOPIC
import com.example.sledipari.utility.extensions.toLocalizable
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSerializationApi::class)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().subscribeToTopic(SLEDI_PARI_TOPIC)

        setContent {

            val navController = rememberNavController()
            val getMonthViewModel: GetMonthViewModel = viewModel()
            val getAllMonthsViewModel: GetAllMonthsViewModel = viewModel()
            val historyViewModel: HistoryViewModel = viewModel()

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
                        navController = navController,
                        viewModel = getMonthViewModel,
                        activity = this@MainActivity,
                        view = window.decorView.rootView
                    )
                }

                composable("settings_screen") {
                    SettingsScreen(
                        navController = navController
                    )
                }

                composable("history_screen") {
                    HistoryScreen(
                        navController = navController,
                        viewModel = historyViewModel
                    )
                }

                composable(
                    "info_screen/{title}/{map}/{red}/{green}/{blue}",
                    arguments = listOf(
                        navArgument("title") {
                            type = NavType.StringType
                        },
                        navArgument("map") {
                            type = NavType.StringType
                        },
                        navArgument("red") {
                            type = NavType.FloatType
                        },
                        navArgument("green") {
                            type = NavType.FloatType
                        },
                        navArgument("blue") {
                            type = NavType.FloatType
                        }
                    )
                ) {

                    val title = it.arguments?.getString("title") ?: ""

                    val map = it.arguments?.getString("map")?.let { encodedMap ->
                        Json.decodeFromString<LinkedHashMap<String, Float>>(encodedMap)
                    }

                    val rgb = Triple(
                        it.arguments?.getFloat("red") ?: 0f,
                        it.arguments?.getFloat("green") ?: 0f,
                        it.arguments?.getFloat("blue") ?: 0f
                    )

                    InfoScreen(
                        navController = navController,
                        title = title,
                        map = map,
                        rgbColor = rgb
                    )
                }
            }


        }
    }
}
