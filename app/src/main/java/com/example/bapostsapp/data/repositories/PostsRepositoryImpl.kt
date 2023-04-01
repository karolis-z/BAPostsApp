package com.example.bapostsapp.data.repositories

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.entities.UserDto
import com.example.bapostsapp.data.local.PostsLocalDataSource
import com.example.bapostsapp.data.local.UsersLocalDataSource
import com.example.bapostsapp.data.mappers.toPost
import com.example.bapostsapp.data.mappers.toPostEntityList
import com.example.bapostsapp.data.remote.PostsRemoteDataSource
import com.example.bapostsapp.data.remote.UsersRemoteDataSource
import com.example.bapostsapp.domain.entities.Post
import com.example.bapostsapp.domain.entities.result.BasicResult
import com.example.bapostsapp.domain.entities.result.ResultOf
import com.example.bapostsapp.domain.repositories.PostsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
    override suspend fun refreshPosts(): BasicResult = withContext(dispatcher) {

        val postsResult = postsRemoteDataSource.getPosts()
        val postDtoList = when (postsResult) {
            is ResultOf.Failure ->
                return@withContext BasicResult.Failure(exception = postsResult.throwable)
            is ResultOf.Success -> postsResult.data
        }

        postsLocalDataSource.saveNewPosts(posts = postDtoList.toPostEntityList())

        // Getting the user data based on which user ids are available in the retrieved posts
        val userIdsRequired = postDtoList.map { it.userId }.distinct()
        getAndSaveUsersFromTheApi(userIds = userIdsRequired)

        return@withContext BasicResult.Success(message = null)
    }

    private suspend fun getAndSaveUsersFromTheApi(userIds: List<Long>) = withContext(dispatcher) {
        val responses = userIds.map { usersRemoteDataSource.getUser(userId = it) }
        /* Choosing here to basically ignore all unsuccessful retrievals of user data. In a real
        * world scenario this wouldn't be OK, but here we don't have an expectation. I'm making an
        * assumption that the most important content is the list of posts. If that's unavailable -
        * error should be shown to user, but if user data is unavailable, posts can still be shown
        * with some sort of indication that user data is unavailable for a particular post. */
        val newUsers = responses.filterIsInstance<ResultOf.Success<UserDto>>().map { it.data }
        if (newUsers.isNotEmpty()) {
            usersLocalDataSource.saveNewUsers(users = newUsers)
        }
    }

    override fun getPosts(): Flow<List<Post>> {
        /* Returning a combined flow from PostEntities and UserEntities to be able to return a Post
        * domain model which needs a user's name that's available in the UserEntity. */
        return postsLocalDataSource.getPosts()
            .combine(usersLocalDataSource.getUsers()) { posts, users ->
                /* Choosing to return an empty list if either of posts of users is empty. */
                if (posts.isEmpty() || users.isEmpty()) {
                    emptyList()
                } else {
                    posts.map { post ->
                        // Finding the user with userId from the post and then getting their name
                        val userName = users.find { user -> user.user.id == post.userId }?.user?.name
                        post.toPost(userName = userName)
                    }
                }
            }
    }
}