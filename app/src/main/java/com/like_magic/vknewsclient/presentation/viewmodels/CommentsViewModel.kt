package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like_magic.vknewsclient.data.repository.NewsFeedRepository
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.presentation.comments.CommentsScreenState
import kotlinx.coroutines.launch

class CommentsViewModel(feedPost: FeedPost, application: Application) : AndroidViewModel(application) {

    private val repository = NewsFeedRepository(application)

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val comments = repository.getComments(feedPost)
            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = comments
            )
        }
    }
}