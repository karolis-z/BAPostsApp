package com.example.bapostsapp.domain.repositories

import com.example.bapostsapp.domain.entities.Post
import com.example.bapostsapp.domain.entities.ResultOf

interface PostsRepository {

    /**
     * @return a [ResultOf.Success] containing a list of [Post] if successful, or [ResultOf.Failure]
     * if not
     */
    suspend fun getPosts(): ResultOf<List<Post>>

}