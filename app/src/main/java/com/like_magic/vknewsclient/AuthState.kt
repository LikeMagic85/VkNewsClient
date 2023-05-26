package com.like_magic.vknewsclient

sealed class AuthState {

    object Authorized:AuthState()

    object NotAuthorized:AuthState()

    object Initial:AuthState()

}
