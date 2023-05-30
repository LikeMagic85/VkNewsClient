package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like_magic.vknewsclient.data.repository.NewsFeedRepository
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.presentation.news.PostsScreenState
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val initialState = PostsScreenState.Initial
    private val _screenState = MutableLiveData<PostsScreenState>(initialState)
    val screenState: LiveData<PostsScreenState>
        get() = _screenState
    private val repository = NewsFeedRepository(getApplication())

    init {
        _screenState.value = PostsScreenState.Loading
        loadRecommendations()
    }


    private fun loadRecommendations(){
        viewModelScope.launch {
            val feedPosts = repository.loadRecommendation()
            _screenState.value = PostsScreenState.Posts(feedPosts)
        }
    }

    fun loadNextRecommendations(){
        _screenState.value = PostsScreenState.Posts(
            posts = repository.feedPosts,
            nextDataIsLoading = true
        )
        loadRecommendations()
    }

    fun changeLikeStatus(feedPost: FeedPost){
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _screenState.value = PostsScreenState.Posts(posts = repository.feedPosts)
        }
    }

    fun remove(feedPost: FeedPost){
        viewModelScope.launch {
            repository.deletePost(feedPost)
            _screenState.value = PostsScreenState.Posts(posts = repository.feedPosts)
        }
    }

}