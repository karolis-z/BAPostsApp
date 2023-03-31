package com.example.bapostsapp.data.entities

import androidx.room.Entity

@Entity(tableName = "posts")
data class PostEntity(
    val id: Long,
    val userId: Long,
    val title: String,
    val body: String,
)
