package com.example.bapostsapp.ui.postsscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bapostsapp.domain.entities.Post

private val AvatarCircleSize = 40.dp

@Composable
fun PostsScreen(
    modifier: Modifier = Modifier
) {

    val viewModel: PostsViewModel = viewModel()
    val posts by viewModel.posts.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = posts,
                key = { post -> post.id }
            ) { post: Post ->
                /* TODO: Leaving the onClick not implemented, will consider implementing expanding
                *   of card to show body of post later. */
                PostCard(post = post, onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            headlineContent = { Text(text = post.title) },
            supportingContent = { Text(text = post.userName) },
            leadingContent = {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(AvatarCircleSize),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = post.userName.first().uppercase(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun PostCardPreview() {
    val post = Post(
        id = 1,
        userId = 1,
        userName = "James Bond",
        title = "qui est esse",
        body = "est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea " +
                "dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut " +
                "reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla"
    )
    PostCard(
        post = post,
        onClick = {}
    )
}