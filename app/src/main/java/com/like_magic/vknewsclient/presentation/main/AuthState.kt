package com.like_magic.vknewsclient.presentation.main

sealed class AuthState {

    object Authorized: AuthState()

    object NotAuthorized: AuthState()

    object Initial: AuthState()

}
