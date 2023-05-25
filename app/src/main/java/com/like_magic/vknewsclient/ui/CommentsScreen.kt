package com.like_magic.vknewsclient.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.like_magic.vknewsclient.domain.FeedPost
import com.like_magic.vknewsclient.domain.PostComment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsScreen(
    feedPost: FeedPost,
    comments: List<PostComment>,
    onBackPressed:()->Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Comments for FeedPost Id: ${feedPost.id}")
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 86.dp
            )
        ) {
            items(items = comments, key = { it.id }) { comment ->
                CommentItem(comment = comment)
            }
        }
    }
}

@Composable
private fun CommentItem(comment: PostComment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = comment.avatar),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = comment.authorName,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.commentText,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.publicationTime,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 12.sp
            )
        }
    }
}