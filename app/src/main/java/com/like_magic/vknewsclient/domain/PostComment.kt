package com.like_magic.vknewsclient.domain

import com.like_magic.vknewsclient.R

data class PostComment(
    val id: Int,
    val authorName: String,
    val avatar: Int = R.drawable.comment_author_avatar,
    val commentText:String = "Long comment text",
    val publicationTime:String = "14:00"
)
