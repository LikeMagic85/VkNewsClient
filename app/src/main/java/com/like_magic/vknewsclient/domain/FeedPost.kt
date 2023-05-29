package com.like_magic.vknewsclient.domain


data class FeedPost(
    val id: String,
    val groupName: String,
    val publicationTime: String,
    val avatarUrl: String,
    val contentText: String,
    val imageUrl: String?,
    val statistic: List<StatisticItem>
)