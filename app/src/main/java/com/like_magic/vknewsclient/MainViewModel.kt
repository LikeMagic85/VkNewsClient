package com.like_magic.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.StatisticItem
import java.lang.IllegalStateException

class MainViewModel:ViewModel() {

    private val _feedPost = MutableLiveData(FeedPost())
    val feedPost:LiveData<FeedPost>
        get() = _feedPost

    fun updateCount(item:StatisticItem){
        val oldStatics = feedPost.value?.statistic ?: throw IllegalStateException()
        val newStatistics = oldStatics.toMutableList().apply {
            replaceAll{oldItem ->
                if(oldItem.type == item.type){
                    oldItem.copy(count = oldItem.count + 1)
                }else {
                    oldItem
                }
            }
        }
        _feedPost.value = feedPost.value?.copy(statistic = newStatistics)
    }

}