package com.example.bapostsapp.ui.postsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bapostsapp.domain.entities.Post
import com.example.bapostsapp.domain.entities.ResultOf
import com.example.bapostsapp.domain.repositories.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()

    /* TODO: This is temporary implementation for UI testing purposes that will be changed later */
    init {
        viewModelScope.launch {
            val result = postsRepository.getPosts()
            when (result) {
                is ResultOf.Failure -> Unit // TODO
                is ResultOf.Success -> _posts.update { result.data }
            }
        }
    }
}