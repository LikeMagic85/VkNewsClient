package com.like_magic.vknewsclient.ui

import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.PostComment


sealed class CommentsScreenState{
    object Initial:CommentsScreenState()
    data class Comments(val feedPost: FeedPost, val comments:List<PostComment>):CommentsScreenState()
}
