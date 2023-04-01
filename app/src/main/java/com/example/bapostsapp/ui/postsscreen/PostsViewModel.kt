package com.example.bapostsapp.ui.postsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bapostsapp.data.ApiException
import com.example.bapostsapp.domain.entities.result.BasicResult
import com.example.bapostsapp.domain.repositories.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    val posts = postsRepository.getPosts()

    private val refreshState: MutableStateFlow<RefreshState> = MutableStateFlow(RefreshState.Ready)

    val uiState = posts.combine(refreshState) { posts, refreshState ->
        if (posts.isEmpty()) {
            when (refreshState) {
                is RefreshState.Error -> {
                    when (refreshState.error) {
                        RefreshError.NO_INTERNET ->
                            PostsUiState.Error(error = PostsError.NO_INTERNET)
                        RefreshError.FAILED_API_REQUEST ->
                            PostsUiState.Error(error = PostsError.FAILED_API_REQUEST)
                    }
                }
                RefreshState.Loading -> PostsUiState.Refreshing
                RefreshState.Ready -> PostsUiState.Error(error = PostsError.NO_DATA_AVAILABLE)
            }
        } else {
            PostsUiState.Ready(refreshState = refreshState)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PostsUiState.Refreshing
    )

    /* Default value is true, because we are letting the uiState determine whether error dialog
    * should be shown. Only when user manually dismisses the error dialog, is when the value should
    * be changed to false. */
    private val showErrorDialog: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val showErrorDialogState: StateFlow<Boolean> = showErrorDialog.combine(uiState) { showError, uiState ->
        /* Show error dialog only if the ui state is Error and the manually set showErrorDialog is
        * also true. This is needed because even though the UiState can be Error, but the user can
        * click on Dismiss button on the dialog and then we don't want to show the dialog, but the
        * uiState remains Error. */
        showError && uiState is PostsUiState.Error
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun refreshData() {
        viewModelScope.launch {
            refreshState.update { RefreshState.Loading }
            /* When refreshing data we should reset the showing of the error dialog to the default
            * value of 'true' because the result of the refresh should now determine whether error
            * should be shown or not */
            showErrorDialog.update { true }
            val refreshResult = postsRepository.refreshPosts()
            when (refreshResult) {
                is BasicResult.Failure -> {
                    val exception = refreshResult.exception
                    if (exception == null || exception !is ApiException) {
                        /* If for any reason the exception was not provided in the BasicResult or
                        * the exception is not of type ApiException, then we simply return a general
                        * type of error indicating that something went wrong. */
                        refreshState.update { RefreshState.Error(error = RefreshError.FAILED_API_REQUEST) }
                    } else {
                        when (exception) {
                            is ApiException.FailedApiRequestException ->
                                refreshState.update {
                                    RefreshState.Error(error = RefreshError.FAILED_API_REQUEST)
                                }
                            is ApiException.NoConnectionException ->
                                refreshState.update {
                                    RefreshState.Error(error = RefreshError.NO_INTERNET)
                                }
                        }
                    }
                }
                is BasicResult.Success -> refreshState.update { RefreshState.Ready }
            }
        }
    }

    fun showErrorDialog(showError: Boolean) {
        showErrorDialog.update { showError }
    }
}