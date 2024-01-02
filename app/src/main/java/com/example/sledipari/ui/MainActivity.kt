package com.example.sledipari.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.elders.EldersFirebaseRemoteConfig.recommendedUpdate
import com.elders.EldersFirebaseRemoteConfig.update
import com.elders.EldersFirebaseRemoteConfig.updates
import com.example.sledipari.R
import com.example.sledipari.ui.info.InfoScreen
import com.example.sledipari.ui.login.LoginScreen
import com.example.sledipari.ui.main.GetMonthViewModel
import com.example.sledipari.ui.main.MonthScreen
import com.example.sledipari.ui.settings.SettingsScreen
import com.example.sledipari.ui.settings.currencies.CurrencyScreen
import com.example.sledipari.ui.settings.currencies.CurrencyViewModel
import com.example.sledipari.ui.settings.history.HistoryScreen
import com.example.sledipari.ui.settings.history.HistoryViewModel
import com.example.sledipari.ui.settings.profile.ProfileScreen
import com.example.sledipari.ui.settings.profile.ProfileViewModel
import com.example.sledipari.ui.splash.GetAllMonthsViewModel
import com.example.sledipari.ui.splash.SplashScreen
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var updateDialog: AlertDialog? = null
    private val _remoteConfigObserver by lazy { Firebase.remoteConfig.updates.observe(this) { checkForUpdates() } }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @OptIn(ExperimentalSerializationApi::class)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        })

        setContent {

            val navController = rememberNavController()

            val getMonthViewModel: GetMonthViewModel = viewModel()
            val historyViewModel: HistoryViewModel = viewModel()
            val currencyViewModel: CurrencyViewModel = viewModel()
            val getAllMonthsViewModel: GetAllMonthsViewModel = viewModel()
            val profileViewModel: ProfileViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "splash_screen"
            ) {
                composable("splash_screen") {
                    SplashScreen(
                        navController = navController
                    )
                }

                composable("login_screen") {
                    LoginScreen(
                        navController = navController
                    )
                }

                composable("main_screen") {
                    MonthScreen(
                        navController = navController,
                        viewModel = getMonthViewModel,
                        currencyViewModel = currencyViewModel
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

                composable("currency_screen") {
                    CurrencyScreen(
                        navController = navController,
                        viewModel = currencyViewModel
                    )
                }

                composable("profile_screen") {
                    ProfileScreen(
                        navController = navController,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        _remoteConfigObserver
        Firebase.remoteConfig.update()
        checkForUpdates()
    }

    private fun checkForUpdates() {

        Firebase.remoteConfig.recommendedUpdate?.let {
            if (it.isApplicable(this)) {
                updateDialog = updateDialog ?: AlertDialog.Builder(this)
                    .setTitle(R.string.alert_update_title)
                    .setMessage(R.string.alert_update_message)
                    .setNegativeButton(R.string.later) { _, _ -> }
                    .setPositiveButton(R.string.update) { _, _ ->
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.download))
                        startActivity(browserIntent)
                    }
                    .setCancelable(false)
                    .setOnDismissListener { updateDialog = null }
                    .create()

                updateDialog?.show()
            }
        }
    }
}

@Composable
fun AppToolbar(
    title: String,
    navController: NavController
) {

    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.label)
            )
        },
        navigationIcon = if (navController.previousBackStackEntry != null) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(id = R.color.label)
                    )
                }
            }
        } else {
            null
        },
        backgroundColor = colorResource(id = R.color.system_gray5),
        contentColor = colorResource(id = R.color.label),
        elevation = 12.dp
    )
}
