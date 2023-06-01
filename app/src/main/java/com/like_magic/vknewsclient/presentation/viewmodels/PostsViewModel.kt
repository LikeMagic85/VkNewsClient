package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.like_magic.vknewsclient.data.repository.NewsFeedRepository
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.extensions.mergeWith
import com.like_magic.vknewsclient.presentation.news.PostsScreenState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepository(getApplication())
    private val recommendationFlow = repository.recommendation
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
            repository.loadNextRecommendations()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost){
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)

        }
    }

    fun remove(feedPost: FeedPost){
        viewModelScope.launch {
            repository.deletePost(feedPost)

        }
    }

}