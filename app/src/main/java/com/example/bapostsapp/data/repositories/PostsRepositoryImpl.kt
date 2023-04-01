package com.example.bapostsapp.data.repositories

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.local.PostsLocalDataSource
import com.example.bapostsapp.data.local.UsersLocalDataSource
import com.example.bapostsapp.data.mappers.toPostEntityList
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
    private val postsLocalDataSource: PostsLocalDataSource,
    private val usersLocalDataSource: UsersLocalDataSource,
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

        postsLocalDataSource.saveNewPosts(posts = postDtoList.toPostEntityList())

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
        /* Choosing here to basically ignore all unsuccessful retrievals of user data. In a real
        * world scenario this wouldn't be OK, but here we don't have an expectation. I'm making an
        * assumption that the most important content is the list of posts. If that's unavailable -
        * error should be shown to user, but if user data is unavailable, posts can still be shown
        * with some sort of indication that user data is unavailable for a particular post. */
        responses.forEach { userResult ->
            if (userResult is ResultOf.Success) {
                usersLocalDataSource.saveNewUser(user = userResult.data)
            }
        }
    }

}