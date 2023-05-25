package com.like_magic.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.ui.PostsScreenState

class PostsViewModel : ViewModel() {

    private val sourceList = mutableListOf<FeedPost>().apply {
        repeat(30){
            add(
                FeedPost(id = it)
            )
        }
    }
    private val initialState = PostsScreenState.Posts(sourceList)
    private val _screenState = MutableLiveData<PostsScreenState>(initialState)
    val screenState: LiveData<PostsScreenState>
        get() = _screenState



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