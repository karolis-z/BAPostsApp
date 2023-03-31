package com.example.bapostsapp.data.repositories

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.domain.entities.Post
import com.example.bapostsapp.domain.entities.ResultOf
import com.example.bapostsapp.domain.repositories.PostsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PostsRepository {

    /* TODO: Added temporary implementation to return a fake list of data for testing purposes.
    *   Change this with actual implementation later*/
    override suspend fun getPosts(): ResultOf<List<Post>> = withContext(dispatcher) {
        val fakeList = listOf(
            Post(
                id = 1,
                userId = 1,
                userName = "James Bond",
                title = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
                body = "quia et suscipit nsuscipit recusandae consequuntur expedita et cum nreprehenderit molestiae ut ut quas totam nnostrum rerum est autem sunt rem eveniet architecto"
            ),
            Post(
                id = 12,
                userId = 2,
                userName = "James Bond",
                title = "in quibusdam tempore odit est dolorem",
                body = "itaque id aut magnam npraesentium quia et ea odit et ea voluptas et nsapiente quia nihil amet occaecati quia id voluptatem nincidunt ea est distinctio odio"
            )
        )
        return@withContext ResultOf.Success(data = fakeList)
    }

}