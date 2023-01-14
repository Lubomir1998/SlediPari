package com.example.sledipari.ui.splash

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sledipari.utility.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ratesTimestamp = sharedPreferences.getLong(Constants.LAST_GET_RATES_DATE, 0L)

        setContent {

            val getAllMonthsViewModel: GetAllMonthsViewModel = viewModel()

            SplashScreen(
                activity = this,
                viewModel = getAllMonthsViewModel,
                ratesTimestamp = ratesTimestamp
            )
        }
    }
}