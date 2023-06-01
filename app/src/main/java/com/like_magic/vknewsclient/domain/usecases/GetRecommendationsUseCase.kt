package com.like_magic.vknewsclient.domain.usecases

import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecommendationsUseCase(private val repository: NewsFeedRepository) {

    operator fun invoke():StateFlow<List<FeedPost>>{
        return repository.getRecommendations()
    }
}