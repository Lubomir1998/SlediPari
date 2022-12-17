package com.example.sledipari.ui.info

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.ui.getRGB
import com.example.sledipari.utility.extensions.formatPrice
import com.example.sledipari.utility.extensions.toLocalizable
import com.example.sledipari.utility.toReadableDate

@Composable
fun InfoScreen(
    navController: NavController,
    title: String,
    map: LinkedHashMap<String, Float>?,
    rgbColor: Triple<Float, Float, Float>
) {

    val maxValue = if (map?.isNotEmpty() == true) {
        map.maxOf { entry ->
            entry.value
        }
    } else 0f

    val statistics = mutableListOf<Pair<String, Float>>()

    if (map?.isNotEmpty() == true) {
        map.map { entry ->
            statistics.add(Pair(entry.key, entry.value))
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title.toLocalizable(LocalContext.current),
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

        map?.let {
            MainContent(
                rgbColor = rgbColor,
                statistics = statistics,
                maxValue = maxValue,
                title = title
            )
        }
    }
}

@Composable
fun MainContent(
    rgbColor: Triple<Float, Float, Float>,
    statistics: List<Pair<String, Float>>,
    maxValue: Float,
    title: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.system_gray5))
            .padding(16.dp)
    ) {
        itemsIndexed(statistics) { index, item ->

            when (item.second) {
                0f -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 12.dp,
                                horizontal = 8.dp
                            )
                    ) {
                        Text(
                            text = item.first.toReadableDate(),
                            color = colorResource(id = R.color.label),
                            fontSize = 16.sp
                        )

                        Text(
                            text = "0",
                            color = colorResource(id = R.color.label),
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    SingleMonth(
                        month = item.first,
                        value = item.second,
                        maxValue = maxValue,
                        color = Color(rgbColor.first, rgbColor.second, rgbColor.third),
                        title = title,
                        animDelay = index * 100,
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            )
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(id = R.color.secondarylabel))
            )
            
        }
    }
}

@Preview
@Composable
fun MainContentPreview() {
    MainContent(
        rgbColor = "home".getRGB(),
        statistics = listOf(
            Pair("2022-08", 13f),
            Pair("2022-07", 20f),
            Pair("2022-06", 17f),
            Pair("2022-05", 0.0f),
            Pair("2022-04", 15f),
        ),
        maxValue = 20f,
        title = "home"
    )
}

@Composable
fun SingleMonth(
    month: String,
    value: Float,
    maxValue: Float,
    color: Color,
    title: String,
    animDelay: Int,
    animDuration: Int = 1000,
    modifier: Modifier = Modifier
) {

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val percent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            value / maxValue
        } else {
            0f
        },
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(
            text = month.toReadableDate(),
            fontSize = 16.sp,
            maxLines = 1,
            color = colorResource(id = R.color.label),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp)
                    .padding(end = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(percent.value)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(color)
                )
            }

            Text(
                text = value.formatPrice(),
                fontSize = 16.sp,
                maxLines = 1,
                color = colorResource(id = R.color.label)
            )
        }
    }

}