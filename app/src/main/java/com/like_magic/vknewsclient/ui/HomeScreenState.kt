package com.like_magic.vknewsclient.ui

import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.PostComment

sealed class HomeScreenState{
    object Initial:HomeScreenState()
    data class Posts(val posts:List<FeedPost>):HomeScreenState()
    data class Comments(val feedPost: FeedPost, val comments:List<PostComment>):HomeScreenState()
}
