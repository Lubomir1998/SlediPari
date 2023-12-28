package com.example.sledipari.ui.settings.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.ui.AppToolbar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController
) {

    Scaffold(
        topBar = {
            AppToolbar(
                title = LocalContext.current.getString(R.string.profile),
                navController = navController
            )
        }, content = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.system_gray5))
            ) {

            }
        }
    )
}