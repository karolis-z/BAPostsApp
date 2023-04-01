package com.example.bapostsapp.domain.entities

data class Post(
    val id: Long,
    val userId: Long,
    val userName: String?,
    val title: String,
    val body: String,
)
