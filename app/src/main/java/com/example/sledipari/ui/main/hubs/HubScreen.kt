package com.example.sledipari.ui.main.hubs

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sledipari.R
import com.example.sledipari.data.models.Hub
import com.example.sledipari.ui.AppToolbar
import com.example.sledipari.ui.settings.profile.RoundImage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HubScreen(
    navController: NavController,
    viewModel: HubsViewModel
) {

    val loading by viewModel.loading.collectAsState()
    val hubs by viewModel.hubs.collectAsState()
    val error by viewModel.getHubsException.collectAsState()

    LaunchedEffect(key1 = true) {

        viewModel.getHubs()
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = LocalContext.current.getString(R.string.hubs),
                navController = navController
            )
        }, content = {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                LazyColumn {
                    itemsIndexed(hubs) { index, item ->

                        when(index) {

                            hubs.size - 1 -> AddHubButton()

                            else -> HubItem(
                                hub = item
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun HubItem(
    hub: Hub,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, Color.White)
            .padding(16.dp)
    ) {

        Text(
            text = hub.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        for (id in hub.users) {
            

        }
    }
}

@Composable
fun AddHubButton(
   modifier: Modifier = Modifier
) {

}