package com.example.sledipari.ui.settings.profile

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.UserProfile
import com.example.sledipari.accessToken
import com.example.sledipari.utility.Constants
import com.example.sledipari.utility.Constants.CLIENT_ID
import com.example.sledipari.utility.Constants.DOMAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?> = _error

    private val account = Auth0(
        CLIENT_ID,
        DOMAIN
    )

    fun logout() {

        accessToken = null
        sharedPreferences.edit().putString(Constants.KEY_REFRESH_TOKEN, null).apply()
    }
    fun getUserInfo() {

        accessToken?.let {

            val client = AuthenticationAPIClient(account)

            // With the access token, call `userInfo` and get the profile from Auth0.
            client.userInfo(it)
                .start(object : Callback<UserProfile, AuthenticationException> {
                    override fun onFailure(exception: AuthenticationException) {
                        _error.value = exception
                    }

                    override fun onSuccess(profile: UserProfile) {

                        _userProfile.value = profile
                    }
                })
        }
    }
}