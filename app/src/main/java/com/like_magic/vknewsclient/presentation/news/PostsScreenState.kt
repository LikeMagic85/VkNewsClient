package com.like_magic.vknewsclient.presentation.news

import com.like_magic.vknewsclient.domain.FeedPost


sealed class PostsScreenState{
    object Initial: PostsScreenState()
    data class Posts(val posts:List<FeedPost>): PostsScreenState()
}
