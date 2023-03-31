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
                message = postsResult.message, throwable = postsResult.throwable
            )
            is ResultOf.Success -> postsResult.data
        }

        // TODO: Add here saving of posts data to the local database

        // Getting the user data based on which user ids are available in the retrieved posts
        val userIdsRequired = postDtoList.map { it.userId }.distinct()
        getAndSaveUsersFromTheApi(userIds = userIdsRequired)

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

    private suspend fun getAndSaveUsersFromTheApi(userIds: List<Long>) = withContext(dispatcher) {
        val responses = userIds.map { usersRemoteDataSource.getUser(userId = it) }
        // TODO: Add here saving of user data to the local database
    }

}