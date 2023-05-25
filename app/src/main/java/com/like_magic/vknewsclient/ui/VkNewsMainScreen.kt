package com.like_magic.vknewsclient.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.navigation.AppNavGraph
import com.like_magic.vknewsclient.navigation.NavigationItem
import com.like_magic.vknewsclient.navigation.Screen

import com.like_magic.vknewsclient.navigation.rememberNavigationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val navigationState = rememberNavigationState()
    val commentsToPost:MutableState<FeedPost?> = remember {
        mutableStateOf(null)
    }
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(70.dp)

            ) {
                val items =
                    listOf(NavigationItem.Home, NavigationItem.Favourite, NavigationItem.Profile)
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any{
                        it.route == item.screen.route
                    } ?: false
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if(!selected){
                                navigationState.navigateTo(item.screen.route)
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                LocalAbsoluteTonalElevation.current
                            )
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                HomeScreen(
                    paddingValues = paddingValues,
                    onCommentClickListener = {
                        commentsToPost.value = it
                        navigationState.navigateTo(Screen.Comments.route)
                    }
                )
            },
            favouriteScreenContent = {

            },
            profileScreenContent = {

            },
            commentsScreenContent = {
                CommentsScreen(
                    feedPost = commentsToPost.value!!,
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    }
                )
            }
        )
    }
}