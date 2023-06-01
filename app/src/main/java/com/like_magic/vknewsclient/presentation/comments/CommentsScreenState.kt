package com.like_magic.vknewsclient.presentation.comments

import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.entity.PostComment


sealed class CommentsScreenState{
    object Initial: CommentsScreenState()
    data class Comments(val feedPost: FeedPost, val comments:List<PostComment>):
        CommentsScreenState()
}
