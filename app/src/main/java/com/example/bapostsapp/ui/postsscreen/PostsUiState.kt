package com.example.bapostsapp.ui.postsscreen


sealed class PostsUiState {
    data class Ready(val refreshState: RefreshState) : PostsUiState()
    data class Error(val error: PostsError) : PostsUiState()
    object Refreshing : PostsUiState()
}

enum class PostsError {
    NO_INTERNET,
    FAILED_API_REQUEST,
    NO_DATA_AVAILABLE
}

sealed class RefreshState {
    object Ready: RefreshState()
    object Loading: RefreshState()
    data class Error(val error: RefreshError): RefreshState()
}

enum class RefreshError {
    NO_INTERNET,
    FAILED_API_REQUEST
}

