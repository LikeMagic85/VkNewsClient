package com.like_magic.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.PostComment
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.ui.HomeScreenState

class MainViewModel : ViewModel() {

    private val sourceList = mutableListOf<FeedPost>().apply {
        repeat(30){
            add(
                FeedPost(id = it)
            )
        }
    }
    private val comments = mutableListOf<PostComment>().apply {
        repeat(10){
            add(
                PostComment(id= it, "Author Name")
            )
        }
    }
    private val initialState = HomeScreenState.Posts(sourceList)
    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState>
        get() = _screenState
    private var savedState:HomeScreenState? = initialState

    fun showComments(feedPost: FeedPost){
        savedState = _screenState.value
        _screenState.value = HomeScreenState.Comments(feedPost = feedPost, comments = comments)
    }

    fun closeComments(){
        _screenState.value = savedState
    }


    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val currentState = _screenState.value
        if(currentState !is HomeScreenState.Posts)return
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
        _screenState.value = HomeScreenState.Posts(newPosts)
    }

    fun remove(feedPost: FeedPost){
        val currentState = _screenState.value
        if(currentState !is HomeScreenState.Posts)return
        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)
        _screenState.value = HomeScreenState.Posts(oldPosts)
    }

}