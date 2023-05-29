package com.like_magic.vknewsclient.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like_magic.vknewsclient.data.mappers.NewsFeedMapper
import com.like_magic.vknewsclient.data.network.ApiFactory
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.presentation.news.PostsScreenState
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val initialState = PostsScreenState.Initial
    private val _screenState = MutableLiveData<PostsScreenState>(initialState)
    val screenState: LiveData<PostsScreenState>
        get() = _screenState
    val mapper = NewsFeedMapper()

    init {
        loadRecommendation()
    }


    fun loadRecommendation(){
        viewModelScope.launch {
            val storage = VKPreferencesKeyValueStorage(getApplication())
            val token = VKAccessToken.restore(storage)?:return@launch
            val response = ApiFactory.apiService.loadRecommendations(token.accessToken)
            val feedPosts = mapper.mapResponseToPosts(response)
            _screenState.value = PostsScreenState.Posts(feedPosts)
        }
    }
    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val currentState = _screenState.value
        if(currentState !is PostsScreenState.Posts)return
        val oldPosts = currentState.posts.toMutableList()
        val oldStatistics = feedPost.statistic
        val newStatistic = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }
        val newFeedPost = feedPost.copy(statistic = newStatistic)
        val newPosts = oldPosts.apply {
            replaceAll {
                if(it.id == newFeedPost.id){
                    newFeedPost
                }else{
                    it
                }
            }
        }
        _screenState.value = PostsScreenState.Posts(newPosts)
    }

    fun remove(feedPost: FeedPost){
        val currentState = _screenState.value
        if(currentState !is PostsScreenState.Posts)return
        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)
        _screenState.value = PostsScreenState.Posts(oldPosts)
    }

}