package com.like_magic.vknewsclient.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.like_magic.vknewsclient.R

sealed class NavigationItem(
    val screen: Screen,
    val titleResId:Int,
    val icon:ImageVector
) {
    object Home: NavigationItem(
        screen = Screen.HomeScreen,
        titleResId = R.string.navigation_item_main,
        icon = Icons.Outlined.Home
    )
    object Favorite: NavigationItem(
        screen = Screen.FavoriteScreen,
        titleResId = R.string.navigation_item_favorite,
        icon = Icons.Outlined.Favorite
    )
    object Profile: NavigationItem(
        screen = Screen.ProfileScreen,
        titleResId = R.string.navigation_item_profile,
        icon = Icons.Outlined.Person
    )
}