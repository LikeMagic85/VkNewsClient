package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.like_magic.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.like_magic.vknewsclient.domain.usecases.DeletePostUseCase
import com.like_magic.vknewsclient.domain.usecases.GetRecommendationsUseCase
import com.like_magic.vknewsclient.domain.usecases.LoadNextRecommendationsUseCase
import com.like_magic.vknewsclient.extensions.mergeWith
import com.like_magic.vknewsclient.presentation.news.PostsScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepositoryImpl(getApplication())
    private val getRecommendationsUseCase = GetRecommendationsUseCase(repository)
    private val loadNextRecommendationsUseCase = LoadNextRecommendationsUseCase(repository)
    private val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)
    private val deletePostUseCase = DeletePostUseCase(repository)

    private val recommendationFlow = getRecommendationsUseCase()
    private val loadNextDataFlow = MutableSharedFlow<PostsScreenState>()
    val screenState = recommendationFlow
        .filter { it.isNotEmpty() }
        .map { PostsScreenState.Posts(posts = it) as PostsScreenState}
        .onStart { emit(PostsScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextRecommendations(){
        viewModelScope.launch {
            loadNextDataFlow.emit(
                PostsScreenState.Posts(
                    posts = recommendationFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextRecommendationsUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost){
        viewModelScope.launch {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun remove(feedPost: FeedPost){
        viewModelScope.launch {
            deletePostUseCase(feedPost)
        }
    }

}