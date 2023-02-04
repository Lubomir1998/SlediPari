package com.example.sledipari.ui.splash

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.ui.MainActivity
import javax.inject.Inject

@Composable
fun SplashScreen(
    activity: SplashActivity,
    viewModel: GetAllMonthsViewModel,
    ratesTimestamp: Long
) {

    val isLoading by viewModel.loading.collectAsState()
    val completed by viewModel.completed.collectAsState()
    val getRatesException by viewModel.getRatesException.collectAsState()
    val getMonthsException by viewModel.getMonthsException.collectAsState()

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
            if (completed && getRatesException == null && getMonthsException == null) {
                Intent(activity, MainActivity::class.java).also {
                    activity.apply {
                        startActivity(it)
                        finish()
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
        
        getRatesException?.let {

            ErrorAlertView(
                activity = activity,
                exception = it,
                ratesTimestamp = ratesTimestamp,
                alertTitle = activity.getString(R.string.rates_error_dialog_title)
            )
        }

        getMonthsException?.let {

            ErrorAlertView(
                activity = activity,
                exception = it,
                alertTitle = activity.getString(R.string.months_error_alert_text)
            )
        }

    }

}

@Composable
fun ErrorAlertView(
    activity: SplashActivity,
    exception: Throwable,
    ratesTimestamp: Long = 0L,
    alertTitle: String
) {

    val message = if (ratesTimestamp > 0L) {
        "${exception.localizedMessage}. ${activity.resources.getString(R.string.rates_error_dialog_message, "ratesTimestamp")}"
    } else {
        exception.localizedMessage
    }

    AlertDialog(
        onDismissRequest = {
            Intent(activity, MainActivity::class.java).also {
                activity.apply {
                    startActivity(it)
                    finish()
                }
            }
        },
        title = { Text(text = alertTitle) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = {
                Intent(activity, MainActivity::class.java).also {
                    activity.apply {
                        startActivity(it)
                        finish()
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}