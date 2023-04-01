package com.example.bapostsapp.data.mappers

import com.example.bapostsapp.data.entities.PostDto
import com.example.bapostsapp.data.entities.PostEntity
import com.example.bapostsapp.domain.entities.Post

fun PostEntity.toPost(userName: String?): Post {
    return Post(
        id = this.id,
        userId = this.userId,
        userName = userName,
        title = this.title,
        body = this.body
    )
}

fun PostDto.toPostEntity(): PostEntity {
    return PostEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        body = this.body
    )
}

fun List<PostDto>.toPostEntityList(): List<PostEntity> {
    return this.map { it.toPostEntity() }
}