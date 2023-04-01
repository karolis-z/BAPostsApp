package com.example.bapostsapp.domain.repositories

import com.example.bapostsapp.domain.entities.Post
import com.example.bapostsapp.domain.entities.result.BasicResult
import kotlinx.coroutines.flow.Flow

interface PostsRepository {

    /**
     * @return a [BasicResult.Success] if refresh is successful, or [BasicResult.Failure]
     * if not.
     */
    suspend fun refreshPosts(): BasicResult

    /**
     * @return a [Flow] of list of [Post]s.
     */
    fun getPosts(): Flow<List<Post>>

}