package com.like_magic.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.PostComment
import com.like_magic.vknewsclient.ui.CommentsScreenState

class CommentsViewModel : ViewModel() {

    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState>
        get() = _screenState
    init {
        loadComments(FeedPost())
    }

    fun loadComments(feedPost: FeedPost) {
        val comments = mutableListOf<PostComment>().apply {
            repeat(10) {
                add(PostComment(id = it, authorName = "Author Name"))
            }
        }
        _screenState.value = CommentsScreenState.Comments(comments = comments, feedPost = feedPost)
    }
}