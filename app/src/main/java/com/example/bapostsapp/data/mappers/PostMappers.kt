package com.example.bapostsapp.data.mappers

import com.example.bapostsapp.data.entities.PostEntity
import com.example.bapostsapp.domain.entities.Post

fun Post.toPostEntity(): PostEntity {
    return PostEntity(id = this.id, userId = this.userId, title = this.title, body = this.body)
}

