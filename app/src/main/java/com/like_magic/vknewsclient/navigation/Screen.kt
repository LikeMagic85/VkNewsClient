package com.like_magic.vknewsclient.navigation

sealed class Screen (
    val route:String
){
    object HomeScreen:Screen(ROUTE_HOME_SCREEN)
    object FavoriteScreen:Screen(ROUTE_FAVORITE_SCREEN)
    object ProfileScreen:Screen(ROUTE_PROFILE_SCREEN)

    private companion object {
        const val ROUTE_HOME_SCREEN = "home screen"
        const val ROUTE_FAVORITE_SCREEN = "favorite screen"
        const val ROUTE_PROFILE_SCREEN = "profile screen"
    }
}