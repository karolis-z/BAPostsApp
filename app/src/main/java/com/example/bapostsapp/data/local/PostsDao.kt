package com.example.bapostsapp.data.local

import androidx.room.*
import com.example.bapostsapp.data.entities.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostEntities(posts: List<PostEntity>): List<Long>

    @Query("DELETE FROM 'posts'")
    suspend fun deleteAllPosts()

    /**
     * Deletes all previously stored [PostEntity] objects and stores new ones.
     */
    @Transaction
    suspend fun updateAllPosts(posts: List<PostEntity>) {
        deleteAllPosts()
        insertPostEntities(posts = posts)
    }

    @Query("SELECT * FROM 'posts' ORDER BY id")
    fun getPostsFlow(): Flow<List<PostEntity>>

}