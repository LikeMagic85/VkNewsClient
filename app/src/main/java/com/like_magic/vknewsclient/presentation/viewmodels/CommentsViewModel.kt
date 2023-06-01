package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.like_magic.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.usecases.GetCommentsUseCase
import com.like_magic.vknewsclient.presentation.comments.CommentsScreenState
import kotlinx.coroutines.flow.map

class CommentsViewModel(feedPost: FeedPost, application: Application) :
    AndroidViewModel(application) {

    private val repository = NewsFeedRepositoryImpl(application)
    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map {
            CommentsScreenState.Comments(
                comments = it,
                feedPost = feedPost
            )
        }
}