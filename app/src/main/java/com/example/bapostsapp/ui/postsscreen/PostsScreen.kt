package com.example.bapostsapp.ui.postsscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bapostsapp.R
import com.example.bapostsapp.domain.entities.Post

private val AvatarCircleSize = 40.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostsScreen(
    modifier: Modifier = Modifier
) {

    val viewModel: PostsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle(initialValue = emptyList())

    val showErrorDialog by viewModel.showErrorDialogState.collectAsStateWithLifecycle()
    val isRefreshing by remember { derivedStateOf { uiState is PostsUiState.Refreshing } }

    val pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refreshData() })

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .pullRefresh(pullRefreshState)
    ) {
        Crossfade(
            targetState = uiState,
            modifier = Modifier.fillMaxSize()
        ) { uiState ->
            when (uiState) {
                /* The Refreshing state is now essentially indicated by the pull to refresh
                indicator, so there's no need to add another indicator. */
                PostsUiState.Refreshing -> Unit
                is PostsUiState.Error -> {
                    ErrorContent(
                        uiState = uiState,
                        showErrorDialog = showErrorDialog,
                        pullRefreshState = pullRefreshState,
                        isRefreshing = isRefreshing,
                        onDialogRetryClicked = { viewModel.refreshData() },
                        onDialogCancelClicked = { viewModel.showErrorDialog(false) })
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
                            PostCard(
                                post = post,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        /* This check for uiState is needed because we essentially have two PullRefreshIndicators.
        * One here and the other in ErrorContent because the PullRefreshIndicator required a
        * scrollable container. So when the uiState is Error, the PullRefreshIndicator in
        * ErrorContent will be the one that's active. While generally they would overlap and user
        * wouldn't notice, in future some padding or other changes could unintentionally move one of
        * those two indicators a bit and then on refresh two indicators would be shown which we want
        * to avoid. */
        if (uiState !is PostsUiState.Error) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorContent(
    uiState: PostsUiState.Error,
    showErrorDialog: Boolean,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
    onDialogRetryClicked: () -> Unit,
    onDialogCancelClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .verticalScroll(rememberScrollState())
    ) {
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
                    TextButton(onClick = onDialogRetryClicked) {
                        Text(
                            text = stringResource(
                                id = R.string.posts_error_dialog_button_retry
                            )
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDialogCancelClicked) {
                        Text(
                            text = stringResource(
                                id = R.string.posts_error_dialog_button_cancel
                            )
                        )
                    }
                }
            )
        }
        /* PullRefresh requires a scrollable container. Therefore implementing another Indicator
        * here within a Box that wraps the entire Error state content. */
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier
) {
    var showBody by remember { mutableStateOf(false) }
    Card(
        onClick = { showBody = !showBody },
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            headlineContent = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = post.title,
                        maxLines = if (showBody) Int.MAX_VALUE else 2,
                        minLines = if (showBody) 1 else 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier.animateContentSize()
                    )
                    AnimatedVisibility(visible = showBody) {
                        Column {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

            },
            supportingContent = {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.userName ?: stringResource(id = R.string.posts_unknown_user_name),
                    style = MaterialTheme.typography.bodySmall
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
        post = post
    )
}