package com.like_magic.vknewsclient.presentation.news

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.like_magic.vknewsclient.R
import com.like_magic.vknewsclient.domain.entity.FeedPost
import com.like_magic.vknewsclient.domain.entity.StatisticItem
import com.like_magic.vknewsclient.domain.entity.StatisticType
import com.like_magic.vknewsclient.ui.theme.DarkRed

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
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
            AsyncImage(
                model = feedPost.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))
            Statistics(
                items = feedPost.statistic,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
                isFavourite = feedPost.isLiked
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
        AsyncImage(
            model = feedPost.avatarUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape),
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
    items: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    isFavourite: Boolean
) {
    Row() {
        Row(modifier = Modifier.weight(1f)) {
            val itemViews = items.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_view_count,
                count = itemViews.count
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
                count = itemShares.count
            )
            IconWithText(
                iconResId = R.drawable.ic_comment,
                count = itemComments.count,
                onItemClickListener = {
                    onCommentClickListener(itemComments)
                }
            )
            IconWithText(
                iconResId = if (!isFavourite) {
                    R.drawable.ic_like
                } else {
                    R.drawable.ic_like_set
                },
                count = itemLikes.count,
                onItemClickListener = {
                    onLikeClickListener(itemLikes)
                },
                tint = if(isFavourite) DarkRed else MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type } ?: throw IllegalStateException()
}

@Composable
private fun IconWithText(
    iconResId: Int,
    count: Int,
    onItemClickListener: (() -> Unit)? = null,
    tint:Color = MaterialTheme.colorScheme.onSecondary
) {
    val modifier = if(onItemClickListener == null){
        Modifier
    }else {
        Modifier.clickable {
            onItemClickListener()
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatStatCount(count),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 14.sp
        )
    }
}

private fun formatStatCount(count: Int): String {
    return if (count < 1000) {
        count.toString()
    } else if (count in 1001..99999) {
        String.format("%.1fK", (count / 1000f))
    } else {
        String.format("%sK", (count / 1000))
    }
}
