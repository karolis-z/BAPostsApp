package com.example.bapostsapp.data.remote

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.entities.PostDto
import com.example.bapostsapp.domain.entities.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PostsRemoteDataSource {
    /**
     * Fetches a list of [PostDto] objects from the api and returns either a Success or Failure in
     * a [ResultOf] wrapper.
     */
    suspend fun getPosts(): ResultOf<List<PostDto>>
}

class PostsRemoteDataSourceImpl @Inject constructor(
    private val postsApi: PostsApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PostsRemoteDataSource {

    override suspend fun getPosts(): ResultOf<List<PostDto>> = withContext(dispatcher) {
        val apiResponse = try {
            postsApi.getPosts()
        } catch (e: Exception) {
            // TODO: implement exception handling
            null
        }

        // TODO: this is temporary implementation for testing, implement proper checks and exception
        //  handling
        val data = apiResponse?.body()
        return@withContext ResultOf.Success(data = data!!)
    }
}