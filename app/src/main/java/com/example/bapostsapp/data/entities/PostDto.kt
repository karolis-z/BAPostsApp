package com.example.bapostsapp.data.entities

data class PostDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val body: String,
)
