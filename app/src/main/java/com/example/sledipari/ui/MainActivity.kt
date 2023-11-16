package com.example.sledipari.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.elders.EldersFirebaseRemoteConfig.recommendedUpdate
import com.elders.EldersFirebaseRemoteConfig.update
import com.elders.EldersFirebaseRemoteConfig.updates
import com.example.sledipari.R
import com.example.sledipari.ui.destinations.InfoScreenDestination
import com.example.sledipari.ui.info.InfoScreen
import com.example.sledipari.utility.Constants.SLEDI_PARI_TOPIC
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.ExperimentalSerializationApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var updateDialog: AlertDialog? = null
    private val _remoteConfigObserver by lazy { Firebase.remoteConfig.updates.observe(this) { checkForUpdates() } }

    @OptIn(ExperimentalSerializationApi::class)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().subscribeToTopic(SLEDI_PARI_TOPIC)
        Firebase.remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        })

        setContent {
            DestinationsNavHost(navGraph = NavGraphs.root)
//            {
//                composable(InfoScreenDestination) {
//                    InfoScreen(
//                        navigator = destinationsNavigator,
//                        navController = navController,
//                        title = navArgs.title,
//                        encodedMap = navArgs.encodedMap,
//                        red = navArgs.red,
//                        green = navArgs.green,
//                        blue = navArgs.blue
//                    )
//                }
//            }
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
    navigator: DestinationsNavigator,
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
                IconButton(onClick = { navigator.navigateUp() }) {
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
