package com.example.sledipari.ui.settings.history

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.utility.extensions.formatPrice
import com.example.sledipari.utility.extensions.toLocalizable
import com.example.sledipari.utility.formatDate

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel
) {

    val historyItems by viewModel.history.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    error?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.history),
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
        LaunchedEffect(key1 = true) {
            viewModel.getHistory()
        }

        if (historyItems.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.system_gray5))
            ) {
                items(historyItems) { item ->

                    HistoryItem(transaction = item)
                }
            }
        }
        else {

            EmptyState()
        }
    }

}

@Composable
fun HistoryItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.section))
    ) {

        Divider(
            modifier = Modifier
                .padding(12.dp)
                .width(10.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(transaction.red, transaction.green, transaction.blue))
        )

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = transaction.title.toLocalizable(LocalContext.current),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.label),
                    fontWeight = FontWeight.Bold
                )

                if (transaction.undo) {

                    Spacer(modifier = Modifier.width(24.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.undo_icon),
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 20.dp
                    )
            ) {
                Text(
                    text = "${transaction.price.formatPrice()} ",
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.label)
                )

                Text(
                    text = stringResource(id = R.string.leva),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.label)
                )

                Spacer(modifier = Modifier.width(24.dp))

                Text(
                    text = transaction.timestamp.formatDate("d MMM yyyy, HH:mm"),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.label)
                )
            }
        }

    }
}

@Composable
fun EmptyState(
    modifier: Modifier = Modifier
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.system_gray5))
    ) {

        Text(
            text = stringResource(id = R.string.nothing_for_now),
            color = colorResource(id = R.color.label),
            fontSize = 16.sp
        )
    }
}