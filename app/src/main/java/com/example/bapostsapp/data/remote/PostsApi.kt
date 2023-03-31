package com.example.bapostsapp.data.remote

import com.example.bapostsapp.data.entities.PostDto
import com.example.bapostsapp.data.entities.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApi {

    @GET("posts")
    suspend fun getPosts(): Response<List<PostDto>>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Long): Response<UserDto>

}