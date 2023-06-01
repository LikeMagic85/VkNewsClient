package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.like_magic.vknewsclient.presentation.main.AuthState
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState>
        get() = _authState

    init {
        val storage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(storage)
        val loggedIn = token != null && token.isValid
        _authState.value = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
    }

    fun performAuthResult(result: VKAuthenticationResult) {
        if (result is VKAuthenticationResult.Success) {
            _authState.value = AuthState.Authorized
        } else {
            _authState.value = AuthState.NotAuthorized
        }
    }
}