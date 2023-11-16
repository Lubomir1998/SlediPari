package com.example.sledipari.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sledipari.R
import com.example.sledipari.ui.destinations.LoginScreenDestination
import com.example.sledipari.ui.destinations.MonthScreenDestination
import com.example.sledipari.ui.destinations.SplashScreenDestination
import com.example.sledipari.utility.formatDate
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Destination(start = true)
fun SplashScreen(
    navigator: DestinationsNavigator,
    viewModel: GetAllMonthsViewModel = hiltViewModel()
) {

    val isLoading by viewModel.loading.collectAsState()
    val getRatesException by viewModel.getRatesException.collectAsState()
    val getMonthsException by viewModel.getMonthsException.collectAsState()

    val state by viewModel.state.collectAsState()

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {

        val ratesTimestamp by viewModel.ratesTimestamp.collectAsState()

        LaunchedEffect(key1 = true) {

            viewModel.applicationStartOperation()
        }

        LaunchedEffect(key1 = state) {

            when (state) {

                GetAllMonthsViewModel.State.LOGIN -> {
                    navigator.navigate(LoginScreenDestination) {
                        popUpTo(SplashScreenDestination.route) {
                            inclusive = true
                        }
                    }
                }

                GetAllMonthsViewModel.State.MAIN -> {
                    if (getRatesException == null && getMonthsException == null) {
                        navigator.navigate(MonthScreenDestination) {
                            popUpTo(SplashScreenDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                GetAllMonthsViewModel.State.SPLASH -> Unit
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
                navigator = navigator,
                exception = it,
                ratesTimestamp = ratesTimestamp,
                alertTitle = stringResource(id = R.string.rates_error_dialog_title)
            )
        }

        getMonthsException?.let {

            ErrorAlertView(
                navigator = navigator,
                exception = it,
                alertTitle = stringResource(id = R.string.months_error_alert_text)
            )
        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorAlertView(
    navigator: DestinationsNavigator,
    exception: Exception,
    ratesTimestamp: Long = 0L,
    alertTitle: String
) {

    val message = if (ratesTimestamp > 0L) {
        "${exception.localizedMessage}. ${stringResource(id = R.string.rates_error_dialog_message)} ${ratesTimestamp.formatDate("d MMM yyyy, HH:mm")}"
    } else {
        exception.localizedMessage
    }

    AlertDialog(
        onDismissRequest = {
            navigator.navigate(MonthScreenDestination) {
                popUpTo(SplashScreenDestination.route) {
                    inclusive = true
                }
            }
        },
        title = { Text(text = alertTitle) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = {
                navigator.navigate(MonthScreenDestination) {
                    popUpTo(SplashScreenDestination.route) {
                        inclusive = true
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}