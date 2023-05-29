package com.like_magic.vknewsclient.data.mappers

import com.like_magic.vknewsclient.data.models.NewsFeedResponseDto
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto):List<FeedPost>{
        val result = mutableListOf<FeedPost>()
        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups
        for (post in posts){
            val group = groups.find {
                it.id == post.communityId.absoluteValue
            } ?: break
            val feedPost = FeedPost(
                id = post.id,
                groupName = group.name,
                publicationTime = mapTimeStampToDate(post.date * 1000),
                avatarUrl = group.imageUrl,
                contentText = post.text,
                imageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistic = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.likes.count ),
                    StatisticItem(type = StatisticType.VIEWS, post.views.count ),
                    StatisticItem(type = StatisticType.SHARES, post.reposts.count ),
                    StatisticItem(type = StatisticType.COMMENTS, post.comments.count)
                ),
                isLiked = post.likes.userLikes > 0,
                groupId = post.communityId
            )
            result.add(feedPost)
        }
        return result
    }

    private fun mapTimeStampToDate(timeStamp:Long):String{
        val date = Date(timeStamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}