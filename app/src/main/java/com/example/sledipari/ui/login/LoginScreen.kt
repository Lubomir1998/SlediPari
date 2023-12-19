package com.example.sledipari.ui.login

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.sledipari.R
import com.example.sledipari.accessToken
import com.example.sledipari.ui.destinations.LoginScreenDestination
import com.example.sledipari.ui.destinations.SplashScreenDestination
import com.example.sledipari.ui.home
import com.example.sledipari.utility.Constants.CLIENT_ID
import com.example.sledipari.utility.Constants.DOMAIN
import com.example.sledipari.utility.Constants.KEY_REFRESH_TOKEN
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
@Destination
fun LoginScreen(
    destinationsNavigator: DestinationsNavigator,
    viewModel: ShitViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var _error by remember {
        mutableStateOf<String?>(null)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(id = R.color.login_background)
            )
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = home),
            onClick = {

            val account = Auth0(
                CLIENT_ID,
                DOMAIN
            )

            WebAuthProvider
                .login(account)
                .withScheme("app")
                .withScope("offline_access")
                // Launch the authentication passing the callback where the results will be received
                .start(context, object : Callback<Credentials, AuthenticationException> {
                    // Called when there is an authentication failure
                    override fun onFailure(error: AuthenticationException) {
                        _error = error.localizedMessage
                    }

                    // Called when authentication completed successfully
                    override fun onSuccess(result: Credentials) {
                        // Get the access token from the credentials object.
                        // This can be used to call APIs
                        val refreshToken = result.refreshToken

                        accessToken = result.idToken
                        viewModel.updateTokenInfo(refreshToken)
                        destinationsNavigator.navigate(SplashScreenDestination) {
                            popUpTo(LoginScreenDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                })
        },
            modifier = Modifier
                .padding(
                    top = 50.dp
                )) {
            Text(
                text = stringResource(id = R.string.login_button_title),
                fontSize = 24.sp,
                color = colorResource(id = R.color.white)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    bottom = 250.dp
                )
        )
    }



    _error?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
    }

}
@HiltViewModel
class ShitViewModel
@Inject constructor(private val sharedPreferences: SharedPreferences): ViewModel() {

    fun updateTokenInfo(token: String?) {
        sharedPreferences.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }
}