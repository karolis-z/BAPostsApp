package com.example.bapostsapp.ui.postsscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bapostsapp.R
import com.example.bapostsapp.domain.entities.Post

private val AvatarCircleSize = 40.dp

@Composable
fun PostsScreen(
    modifier: Modifier = Modifier
) {

    val viewModel: PostsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle(initialValue = emptyList())

    val showErrorDialog by viewModel.showErrorDialogState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Crossfade(targetState = uiState, modifier = Modifier.fillMaxSize()) { uiState ->
            when (uiState) {
                PostsUiState.Refreshing -> LoadingCircle(modifier = Modifier.fillMaxSize())
                is PostsUiState.Error -> {
                    val errorText = when (uiState.error) {
                        PostsError.NO_INTERNET ->
                            stringResource(
                                id = R.string.posts_error_dialog_no_internet,
                                stringResource(id = R.string.posts_error_dialog_button_retry)
                            )
                        PostsError.FAILED_API_REQUEST ->
                            stringResource(
                                id = R.string.posts_error_dialog_something_wrong,
                                stringResource(id = R.string.posts_error_dialog_button_retry)
                            )
                        PostsError.NO_DATA_AVAILABLE ->
                            stringResource(
                                id = R.string.posts_error_dialog_no_data,
                                stringResource(id = R.string.posts_error_dialog_button_retry)
                            )
                    }
                    AnimatedVisibility(visible = showErrorDialog) {
                        AlertDialog(
                            onDismissRequest = { },
                            text = { Text(text = errorText) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Error,
                                    contentDescription = null // Icon is decorative, no need for cd
                                )
                            },
                            title = {
                                Text(text = stringResource(id = R.string.posts_error_dialog_title))
                            },
                            properties = DialogProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false
                            ),
                            confirmButton = {
                                TextButton(onClick = { viewModel.refreshData() }) {
                                    Text(text = stringResource(
                                        id = R.string.posts_error_dialog_button_retry
                                    ))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { viewModel.showErrorDialog(false) }) {
                                    Text(text = stringResource(
                                        id = R.string.posts_error_dialog_button_cancel
                                    ))
                                }
                            }
                        )
                    }
                }
                is PostsUiState.Ready -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(
                            items = posts,
                            key = { post -> post.id }
                        ) { post: Post ->
                            /* TODO: Leaving the onClick empty, will consider implementing
                                expanding of card to show body of post later. */
                            PostCard(
                                post = post,
                                onClick = { },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
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
            supportingContent = {
                Text(
                    text = post.userName ?: stringResource(id = R.string.posts_unknown_user_name)
                )
            },
            leadingContent = {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(AvatarCircleSize),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = post.userName?.first()?.uppercase()
                                ?: stringResource(id = R.string.posts_unknown_user_initial),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun LoadingCircle(
    modifier: Modifier = Modifier,
    circleSize: Dp = 100.dp,
    color: Color = ProgressIndicatorDefaults.linearColor,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(circleSize),
            color = color,
            strokeWidth = strokeWidth
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