package com.like_magic.vknewsclient.domain.entity


data class FeedPost(
    val id: Long,
    val groupId: Long,
    val groupName: String,
    val publicationTime: String,
    val avatarUrl: String,
    val contentText: String,
    val imageUrl: String?,
    val statistic: List<StatisticItem>,
    val isLiked: Boolean
)