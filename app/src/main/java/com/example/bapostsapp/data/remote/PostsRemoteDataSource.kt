package com.example.bapostsapp.data.remote

import android.util.Log
import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.core.util.ConnectivityChecker
import com.example.bapostsapp.data.ApiException
import com.example.bapostsapp.data.entities.PostDto
import com.example.bapostsapp.domain.entities.result.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "POSTS_REMOTE_DATASOURCE"

interface PostsRemoteDataSource {
    /**
     * Fetches a list of [PostDto] objects from the api and returns either a Success or Failure in
     * a [ResultOf] wrapper.
     */
    suspend fun getPosts(): ResultOf<List<PostDto>>
}

class PostsRemoteDataSourceImpl @Inject constructor(
    private val postsApi: PostsApi,
    private val connectivityChecker: ConnectivityChecker,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PostsRemoteDataSource {

    override suspend fun getPosts(): ResultOf<List<PostDto>> = withContext(dispatcher) {
        if (!connectivityChecker.isOnline()) {
            val error = "Device is offline. Could not retrieve posts from the api."
            Log.e(TAG, "getPosts: $error")
            return@withContext ResultOf.Failure(
                message = error,
                throwable = ApiException.NoConnectionException(message = error, throwable = null)
            )
        }

        val apiResponse = try {
            postsApi.getPosts()
        } catch (e: Exception) {
            return@withContext getFailedApiResponseResult(exception = e)
        }

        /* In case the response would be successful but would not have the 200 code or the body for
        * some reason would be null, then should return a Failure with a FailedApiRequestException.
        * Treating all non 200 codes the same here, because we do not have documentation of the api
        * to distinguish between failed call, server down or no data available etc. */
        val responseBody = apiResponse.body()
        if (apiResponse.code() != 200 || responseBody == null) {
            return@withContext getFailedApiResponseResult(exception = null)
        }

        // At this stage, we are sure the response is successful and contains our data
        return@withContext ResultOf.Success(data = responseBody)
    }

    private fun getFailedApiResponseResult(exception: Exception?): ResultOf.Failure {
        val error = "Failed to retrieve posts from the api."
        Log.e(TAG, "getFailedApiResponseResult: $exception", exception)
        return ResultOf.Failure(
            message = error,
            throwable = ApiException.FailedApiRequestException(
                message = error,
                throwable = exception
            )
        )
    }
}