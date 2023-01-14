package com.example.sledipari.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.sledipari.R
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
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
        }, content = {

            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.system_gray5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorResource(id = R.color.section))
                ) {

                    SettingsItem(titleId = R.string.history) {
                        navController.navigate("history_screen")
                    }

                    SettingsItem(titleId = R.string.currency) {
                        navController.navigate("currency_screen")
                    }
                }
            }
        })
}

@Composable
fun SettingsItem(
    titleId: Int,
    modifier: Modifier = Modifier,
    onClickAction: (() -> Unit)
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onClickAction()
            }
    ) {

        Text(
            text = stringResource(id = titleId),
            color = colorResource(id = R.color.label)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = colorResource(id = R.color.label)
        )
    }
}