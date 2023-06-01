package com.like_magic.vknewsclient.domain.usecases


import com.like_magic.vknewsclient.domain.repository.NewsFeedRepository


class LoadNextRecommendationsUseCase(private val repository: NewsFeedRepository) {

    suspend operator fun invoke() {
        return repository.loadNextRecommendations()
    }
}