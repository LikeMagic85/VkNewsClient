package com.like_magic.vknewsclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.presentation.viewmodels.PostsViewModel
import com.like_magic.vknewsclient.ui.theme.DarkBlue

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClickListener: (FeedPost) -> Unit
){
    val viewModel: PostsViewModel = viewModel()
    val screenState = viewModel.screenState.collectAsState(PostsScreenState.Initial)
    when(val currentState = screenState.value){
        is PostsScreenState.Posts -> {
            FeedPosts(
                posts = currentState.posts,
                viewModel = viewModel,
                paddingValues = paddingValues,
                onCommentClickListener = onCommentClickListener,
                nextDataIsLoading = currentState.nextDataIsLoading
            )
        }
        is PostsScreenState.Initial -> {}
        is PostsScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DarkBlue)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    posts:List<FeedPost>,
    viewModel: PostsViewModel,
    paddingValues: PaddingValues,
    onCommentClickListener:(FeedPost)->Unit,
    nextDataIsLoading:Boolean
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
                    onCommentClickListener = {
                        onCommentClickListener(feedPost)
                    },
                    onLikeClickListener = {
                        viewModel.changeLikeStatus(feedPost)
                    }
                )
            }
        }
        item {
            if(nextDataIsLoading){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(color = DarkBlue)
                }
            }else{
                SideEffect {
                    viewModel.loadNextRecommendations()
                }
            }
        }
    }
}