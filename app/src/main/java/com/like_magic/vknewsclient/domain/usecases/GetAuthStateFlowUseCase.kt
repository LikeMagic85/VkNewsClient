package com.like_magic.vknewsclient.domain.usecases

import com.like_magic.vknewsclient.domain.entity.AuthState
import com.like_magic.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetAuthStateFlowUseCase(private val repository: NewsFeedRepository) {

    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthStateFlow()
    }
}