package com.like_magic.vknewsclient.domain.repository

import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.entity.PostComment
import com.like_magic.vknewsclient.domain.entity.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedRepository {

    fun getAuthStateFlow():StateFlow<AuthState>

    fun getRecommendations():StateFlow<List<FeedPost>>

    fun getComments(feedPost: FeedPost): Flow<List<PostComment>>

    suspend fun loadNextRecommendations()

    suspend fun checkAuthState()

    suspend fun changeLikeStatus(feedPost: FeedPost)

    suspend fun deletePost(feedPost: FeedPost)

}