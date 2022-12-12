package com.example.sledipari.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.utility.extensions.toLocalizable

@Composable
fun InfoScreen(
    navController: NavController,
    title: String,
    rgbColor: Triple<Float, Float, Float>
) {

    Scaffold(
        topBar = {
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
                                contentDescription = "Back"
                            )
                        }
                    }
                } else {
                    null
                },
                backgroundColor = colorResource(id = R.color.system_gray5),
                contentColor = Color.White,
                elevation = 12.dp
            )
        }) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(
                    red = rgbColor.first,
                    green = rgbColor.second,
                    blue = rgbColor.third
                ))
        ) {

        }
    }
}

@Composable
fun SingleMonth() {

}