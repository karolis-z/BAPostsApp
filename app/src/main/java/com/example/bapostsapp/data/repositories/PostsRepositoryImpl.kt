package com.example.bapostsapp.data.repositories

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.remote.PostsRemoteDataSource
import com.example.bapostsapp.data.remote.UsersRemoteDataSource
import com.example.bapostsapp.domain.entities.Post
import com.example.bapostsapp.domain.entities.ResultOf
import com.example.bapostsapp.domain.repositories.PostsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "POSTS_REPOSITORY"

class PostsRepositoryImpl @Inject constructor(
    private val postsRemoteDataSource: PostsRemoteDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PostsRepository {

    /* TODO: Added temporary implementation to return a fake list of data for testing purposes.
    *   Change this with actual implementation later*/
    override suspend fun getPosts(): ResultOf<List<Post>> = withContext(dispatcher) {

        val postsResult = postsRemoteDataSource.getPosts()
        val postDtoList = when (postsResult) {
            is ResultOf.Failure -> return@withContext ResultOf.Failure(
                /* TODO: Add proper exception here later */ null, null
            )
            is ResultOf.Success -> postsResult.data
        }

        // TODO: Temporary implementation to be able to pass along retrieved data to the ui:
        val postsList = mutableListOf<Post>()
        postDtoList.forEach { postDto ->
            postsList.add(
                Post(
                    id = postDto.id,
                    userId = postDto.userId,
                    userName = "James Bond",
                    title = postDto.title,
                    body = postDto.body
                )
            )
        }
        return@withContext ResultOf.Success(data = postsList)
    }

}