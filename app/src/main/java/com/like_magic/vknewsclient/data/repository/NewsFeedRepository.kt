package com.like_magic.vknewsclient.data.repository

import android.app.Application
import com.like_magic.vknewsclient.data.mappers.NewsFeedMapper
import com.like_magic.vknewsclient.data.network.ApiFactory
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.PostComment
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.domain.StatisticType
import com.like_magic.vknewsclient.extensions.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepository(application: Application) {

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)
    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()
    private val scope = CoroutineScope(Dispatchers.Main)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    val recommendation: StateFlow<List<FeedPost>> = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom
            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }
            val response = if (startFrom == null) {
                apiService.loadRecommendations(getAccessToken())
            } else {
                apiService.loadRecommendations(getAccessToken(), startFrom)
            }
            val posts = mapper.mapResponseToPosts(response)
            nextFrom = response.newsFeedContent.nextFrom
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }
        .mergeWith(refreshedListFlow)
        .stateIn(scope = scope, started = SharingStarted.Lazily, initialValue = feedPosts)

    suspend fun loadNextRecommendations(){
        nextDataNeededEvents.emit(Unit)
    }


    private fun getAccessToken(): String {
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
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            accessToken = getAccessToken(),
            ownerId = feedPost.groupId,
            postId = feedPost.id
        )
        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun getComments(feedPost: FeedPost): List<PostComment> {
        val comments = apiService.getComments(
            accessToken = getAccessToken(),
            ownerId = feedPost.groupId,
            postId = feedPost.id
        )
        return mapper.mapResponseToComments(comments)
    }
}