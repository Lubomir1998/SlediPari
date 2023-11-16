package com.example.sledipari.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.example.sledipari.ui.destinations.LoginScreenDestination
import com.example.sledipari.ui.destinations.SplashScreenDestination
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
    ) {
        Button(onClick = {

            val account = Auth0(
                "wQHnVE7ocP1SOZux0oVRsQm5RGKkiFPX",
                "dev-j6hq26y1j8pv5deu.us.auth0.com"
            )

            WebAuthProvider
                .login(account)
                .withScheme("app")
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
                        val accessToken = result.idToken
                        viewModel.updateTokenInfo(accessToken)
                        destinationsNavigator.navigate(SplashScreenDestination) {
                            popUpTo(LoginScreenDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                })
        }) {
            Text(text = "Login", fontSize = 24.sp)
        }
    }



    _error?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
    }

}

private fun loginWithBrowser(context: Context) {
    // Setup the WebAuthProvider, using the custom scheme and scope.

    val account = Auth0(
        "wQHnVE7ocP1SOZux0oVRsQm5RGKkiFPX",
        "dev-j6hq26y1j8pv5deu.us.auth0.com"
    )

    WebAuthProvider
        .login(account)
        .withScheme("app")
        // Launch the authentication passing the callback where the results will be received
        .start(context, object : Callback<Credentials, AuthenticationException> {
            // Called when there is an authentication failure
            override fun onFailure(error: AuthenticationException) {

            }

            // Called when authentication completed successfully
            override fun onSuccess(result: Credentials) {
                // Get the access token from the credentials object.
                // This can be used to call APIs
                val accessToken = result.idToken

            }
        })
}
@HiltViewModel
class ShitViewModel
@Inject constructor(private val sharedPreferences: SharedPreferences): ViewModel() {

    fun updateTokenInfo(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }
}