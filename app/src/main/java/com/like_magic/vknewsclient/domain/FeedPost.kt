package com.like_magic.vknewsclient.domain

import com.like_magic.vknewsclient.R

data class FeedPost(
    val groupName: String = "/dev/null",
    val publicationTime:String = "14:00",
    val avatar:Int = R.drawable.ic_launcher_background,
    val contentText:String = "Contrary to popular belief, Lorem Ipsum is not simply random text",
    val image:Int = R.drawable.post_content_image,
    val statistic: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 350),
        StatisticItem(StatisticType.SHARES, 10),
        StatisticItem(StatisticType.COMMENTS, 5),
        StatisticItem(StatisticType.LIKES, 22)
    )
    )