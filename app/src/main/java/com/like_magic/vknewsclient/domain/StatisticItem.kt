package com.like_magic.vknewsclient.domain

data class StatisticItem (val type: StatisticType, val count:Int = 0)

enum class StatisticType{
    VIEWS, SHARES, COMMENTS, LIKES
}