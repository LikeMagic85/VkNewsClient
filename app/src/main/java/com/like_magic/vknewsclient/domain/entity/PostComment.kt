package com.like_magic.vknewsclient.domain.entity


data class PostComment(
    val id: Long,
    val authorName: String,
    val avatarUrl: String,
    val commentText:String,
    val publicationTime:String
)
