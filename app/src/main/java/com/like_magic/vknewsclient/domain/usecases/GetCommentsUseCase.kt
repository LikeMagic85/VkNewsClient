package com.like_magic.vknewsclient.domain.usecases

import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.entity.PostComment
import com.like_magic.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.Flow

class GetCommentsUseCase(private val repository: NewsFeedRepository) {

    operator fun invoke(feedPost: FeedPost): Flow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}