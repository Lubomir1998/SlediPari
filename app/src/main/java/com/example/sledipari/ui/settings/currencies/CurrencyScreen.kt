package com.example.sledipari.ui.settings.currencies

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.ui.AppToolbar
import com.example.sledipari.utility.Constants
import com.example.sledipari.utility.extensions.flagEmoji
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Destination
fun CurrencyScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: CurrencyViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            AppToolbar(
                title = LocalContext.current.getString(R.string.currency),
                navigator = navigator,
                navController = navController
            )
        }
    ) {

        val rates by viewModel.rates.collectAsState()
        val loading by viewModel.currencyLoading.collectAsState()
        val base by viewModel.currentRate.collectAsState()

        LaunchedEffect(key1 = true) {
            viewModel.getRates()
            viewModel.getCurrentRate()
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.system_gray5))
        ) {
            items(rates.keys.toList()) {

                CurrencyRateItem(
                    rate = it,
                    isBase = base == it,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    viewModel.updateCurrentRate(it)
                }

                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .padding(start = 16.dp)
                        .background(colorResource(id = R.color.divider))
                )
            }
        }
    }
}

@Composable
fun CurrencyRateItem(
    rate: String,
    isBase: Boolean,
    modifier: Modifier = Modifier,
    onClickListener: (() -> Unit)
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClickListener()
            }
    ) {

        Column {

            Row(
                modifier = Modifier
            ) {
                Text(
                    text = rate,
                    color = colorResource(id = R.color.label),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )

                // flag
                Text(
                    text = rate.flagEmoji(),
                    fontSize = 18.sp
                )
            }

            Text(
                text = Currency.getInstance(rate).displayName,
                color = colorResource(id = R.color.label),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }

        if (isBase) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colorResource(id = R.color.label),
                modifier = Modifier
                    .padding(end = 12.dp)
            )
        }
    }
}