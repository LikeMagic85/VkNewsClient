package com.like_magic.vknewsclient.presentation.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.like_magic.vknewsclient.R
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.StatisticItem
import com.like_magic.vknewsclient.domain.StatisticType

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener:(StatisticItem)-> Unit,
    onShareClickListener:(StatisticItem)-> Unit,
    onCommentClickListener:(StatisticItem)-> Unit,
    onViewClickListener:(StatisticItem)-> Unit,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(0.01.dp, MaterialTheme.colorScheme.onBackground),
        modifier = modifier

    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = feedPost.contentText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(
                    id = R.drawable.post_content_image
                ), contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))
            Statistics(
                items = feedPost.statistic,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
                onShareClickListener = onShareClickListener,
                onViewClickListener = onViewClickListener
            )
        }
    }

}

@Composable
private fun PostHeader(feedPost: FeedPost) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape),
            painter = painterResource(id = feedPost.avatar),
            contentDescription = "Group logo"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = feedPost.groupName,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = feedPost.publicationTime,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "menu",
            tint = MaterialTheme.colorScheme.onSecondary
        )

    }
}

@Composable
private fun Statistics(
    items:List<StatisticItem>,
    onLikeClickListener:(StatisticItem)-> Unit,
    onShareClickListener:(StatisticItem)-> Unit,
    onCommentClickListener:(StatisticItem)-> Unit,
    onViewClickListener:(StatisticItem)-> Unit,
) {
    Row() {
        Row(modifier = Modifier.weight(1f)) {
            val itemViews = items.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_view_count,
                text = itemViews.count.toString(),
                onItemClickListener = {
                    onViewClickListener(itemViews)
                }
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val itemShares = items.getItemByType(StatisticType.SHARES)
            val itemComments = items.getItemByType(StatisticType.COMMENTS)
            val itemLikes = items.getItemByType(StatisticType.LIKES)
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = itemShares.count.toString(),
                onItemClickListener = {
                    onShareClickListener(itemShares)
                }
            )
            IconWithText(
                iconResId = R.drawable.ic_comment,
                text = itemComments.count.toString(),
                onItemClickListener = {
                    onCommentClickListener(itemComments)
                }
            )
            IconWithText(
                iconResId = R.drawable.ic_like,
                text = itemLikes.count.toString(),
                onItemClickListener = {
                    onLikeClickListener(itemLikes)
                }
            )
        }
    }
}

private fun List<StatisticItem>.getItemByType(type:StatisticType):StatisticItem {
    return this.find { it.type == type } ?: throw IllegalStateException()
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: ()-> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onItemClickListener()
        }
        ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 16.sp
        )
    }
}
