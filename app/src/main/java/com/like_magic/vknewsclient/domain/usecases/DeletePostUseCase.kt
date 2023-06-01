package com.like_magic.vknewsclient.domain.usecases

import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.repository.NewsFeedRepository


class DeletePostUseCase(private val repository: NewsFeedRepository) {

    suspend operator fun invoke(feedPost: FeedPost) {
        return repository.deletePost(feedPost)
    }
}