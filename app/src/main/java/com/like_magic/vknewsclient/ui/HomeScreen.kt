package com.like_magic.vknewsclient.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.like_magic.vknewsclient.MainViewModel
import com.like_magic.vknewsclient.domain.FeedPost

@Composable
fun HomeScreen(paddingValues: PaddingValues, viewModel: MainViewModel){
    val screenState = viewModel.screenState.observeAsState(HomeScreenState.Initial)
    when(val currentState = screenState.value){
        is HomeScreenState.Posts -> {
            FeedPosts(posts = currentState.posts, viewModel = viewModel, paddingValues = paddingValues)
        }

        is HomeScreenState.Comments -> {
            CommentsScreen(
                feedPost = currentState.feedPost,
                comments = currentState.comments,
                onBackPressed = {
                    viewModel.closeComments()
                }
            )
            BackHandler {
                viewModel.closeComments()
            }
        }

        is HomeScreenState.Initial -> {

        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    posts:List<FeedPost>,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
){
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { feedPost ->
            val dismissState = rememberDismissState()
            if(dismissState.isDismissed(DismissDirection.EndToStart)){
                viewModel.remove(feedPost)
            }
            SwipeToDismiss(
                state = dismissState,
                background = {},
                directions = setOf(DismissDirection.EndToStart),
                modifier = Modifier.animateItemPlacement()
            ) {
                PostCard(
                    modifier = Modifier.padding(8.dp),
                    feedPost = feedPost,
                    onViewClickListener = {statisticItem ->
                        viewModel.updateCount(feedPost, statisticItem)
                    },
                    onShareClickListener = {statisticItem ->
                        viewModel.updateCount(feedPost, statisticItem)
                    },
                    onCommentClickListener = {
                        viewModel.showComments(feedPost)
                    },
                    onLikeClickListener = {statisticItem ->
                        viewModel.updateCount(feedPost, statisticItem)
                    }
                )
            }
        }
    }
}