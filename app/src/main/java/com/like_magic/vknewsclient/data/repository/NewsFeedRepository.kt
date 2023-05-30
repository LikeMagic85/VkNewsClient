package com.like_magic.vknewsclient.data.repository

import android.app.Application
import com.like_magic.vknewsclient.data.mappers.NewsFeedMapper
import com.like_magic.vknewsclient.data.network.ApiFactory
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.domain.StatisticType
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken

class NewsFeedRepository(application: Application) {

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)
    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom:String? = null

    suspend fun loadRecommendation():List<FeedPost>{
        val startFrom = nextFrom
        if(startFrom == null && feedPosts.isNotEmpty()) return feedPosts
        val response = if(startFrom == null) {
            apiService.loadRecommendations(getAccessToken())
        }else{
            apiService.loadRecommendations(getAccessToken(), startFrom)
        }
        val posts = mapper.mapResponseToPosts(response)
        nextFrom = response.newsFeedContent.nextFrom
        _feedPosts.addAll(posts)
        return feedPosts
    }

    private fun getAccessToken():String {
        return token?.accessToken ?: throw IllegalStateException("token is null")
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.groupId,
                postId = feedPost.id
            )
        } else {
            apiService.addLike(
                token = getAccessToken(),
                ownerId = feedPost.groupId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistic.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, newLikesCount))
        }
        val newPost = feedPost.copy(statistic = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }
}