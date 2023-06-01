package com.like_magic.vknewsclient.data.mappers

import com.like_magic.vknewsclient.data.models.CommentsResponseDto
import com.like_magic.vknewsclient.data.models.NewsFeedResponseDto
import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.entity.PostComment
import com.like_magic.vknewsclient.domain.entity.StatisticItem
import com.like_magic.vknewsclient.domain.entity.StatisticType
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
                publicationTime = post.date.toString(),
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

    fun mapResponseToComments(response: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()
        val comments = response.content.comments
        val profiles = response.content.profiles
        for (comment in comments) {
            if (comment.text.isBlank()) continue
            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                avatarUrl =  author.avatarUrl,
                commentText = comment.text,
                publicationTime = mapTimestampToDate(comment.date)
            )
            result.add(postComment)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}