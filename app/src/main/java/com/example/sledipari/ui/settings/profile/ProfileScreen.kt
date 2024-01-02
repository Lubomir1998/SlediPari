package com.example.sledipari.ui.settings.profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.UserProfile
import com.example.sledipari.R
import com.example.sledipari.ui.AppToolbar
import com.example.sledipari.ui.home
import com.example.sledipari.ui.splash.GetAllMonthsViewModel
import com.example.sledipari.utility.Constants
import io.ktor.http.ContentType

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {

    val profile by profileViewModel.userProfile.collectAsState()
    val error by profileViewModel.error.collectAsState()

    LaunchedEffect(key1 = true) {

        profileViewModel.getUserInfo()
    }

    error?.localizedMessage?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
    }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

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

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .scrollable(scrollState, Orientation.Vertical)
                        .fillMaxSize()
                ) {


                    UserInfo(profile = profile)

                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        onClick = {

                            val account = Auth0(
                                Constants.CLIENT_ID,
                                Constants.DOMAIN
                            )

                            WebAuthProvider.logout(account)
                                .withScheme("app")
                                .start(context, object : Callback<Void?, AuthenticationException> {
                                    override fun onSuccess(payload: Void?) {

                                        profileViewModel.logout()
                                        navController.navigate("login_screen") {
                                            popUpTo("settings_screen") {
                                                inclusive = true
                                            }
                                            popUpTo("main_screen") {
                                                inclusive = true
                                            }
                                        }
                                    }

                                    override fun onFailure(error: AuthenticationException) {
                                        Toast.makeText(
                                            context,
                                            error.localizedMessage,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                })
                        },
                        modifier = Modifier
                            .padding(
                                bottom = 24.dp
                            )
                    ) {
                        Text(
                            text = stringResource(id = R.string.logout_button_title),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun UserInfo(profile: UserProfile?) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {

        RoundImage(
            size = 90.dp,
            imgUrl = profile?.pictureURL,
            modifier = Modifier
                .padding(
                    top = 8.dp
                )
        )

        profile?.name?.let { name ->
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.label),
                modifier = Modifier
                    .padding(
                        top = 16.dp
                    )
            )
        }

        profile?.email?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(
                        top = 8.dp
                    )
            ) {

                Icon(
                    Icons.Default.MailOutline,
                    contentDescription = null,
                    tint = colorResource(id = R.color.secondarylabel),
                    modifier = Modifier
                        .size(16.dp)
                )
                
                Spacer(modifier = Modifier.padding(16.dp))

                Text(
                    text = it,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.secondarylabel)
                )
            }
        }
    }
}

@Composable
fun RoundImage(
    size: Dp,
    imgUrl: String?,
    modifier: Modifier = Modifier
) {
    Image(
        painter = if (imgUrl != null) rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = imgUrl)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                    placeholder(R.drawable.friend_avatar_placeholder)
                    transformations(CircleCropTransformation())
                }).build()
        ) else painterResource(id = R.drawable.friend_avatar_placeholder),
        contentDescription = null,
        modifier = modifier.size(size)
    )
}