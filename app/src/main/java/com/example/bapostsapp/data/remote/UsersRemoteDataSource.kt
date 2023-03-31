package com.example.bapostsapp.data.remote

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.entities.UserDto
import com.example.bapostsapp.domain.entities.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UsersRemoteDataSource {
    /**
     * Retrieves a [UserDto] from the api based on provided [userId] in a [ResultOf] wrapper
     * indicating a Success or Failure.
     */
    suspend fun getUser(userId: Long): ResultOf<UserDto>
}

class UserRemoteDataSourceImpl @Inject constructor(
    private val postsApi: PostsApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UsersRemoteDataSource {

    override suspend fun getUser(userId: Long): ResultOf<UserDto> = withContext(dispatcher) {
        val apiResponse = try {
            postsApi.getUser(userId = userId)
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