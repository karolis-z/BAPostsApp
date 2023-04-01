package com.example.bapostsapp.data.local

import com.example.bapostsapp.core.di.IoDispatcher
import com.example.bapostsapp.data.entities.PostEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface PostsLocalDataSource {

    /**
     * Saves a list of [PostEntity] objects by first deleting all other existing posts, essentially
     * refreshing the entire database.
     */
    suspend fun saveNewPosts(posts: List<PostEntity>)

    /**
     * Returns a [Flow] containing a list of [PostEntity] objects from the local database.
     */
    fun getPosts(): Flow<List<PostEntity>>

}

class PostsLocalDataSourceImpl @Inject constructor(
    private val postsDao: PostsDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PostsLocalDataSource {

    override suspend fun saveNewPosts(posts: List<PostEntity>) = withContext(dispatcher) {
        postsDao.updateAllPosts(posts = posts)
    }

    override fun getPosts(): Flow<List<PostEntity>> = postsDao.getPostsFlow()

}