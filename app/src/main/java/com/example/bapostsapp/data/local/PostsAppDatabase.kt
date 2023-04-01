package com.example.bapostsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bapostsapp.data.entities.AddressEntity
import com.example.bapostsapp.data.entities.CompanyEntity
import com.example.bapostsapp.data.entities.PostEntity
import com.example.bapostsapp.data.entities.UserEntity

@Database(
    entities = [
        PostEntity::class,
        UserEntity::class,
        CompanyEntity::class,
        AddressEntity::class
    ],
    version = 1
)
abstract class PostsAppDatabase : RoomDatabase() {

    abstract fun postsDao(): PostsDao

    abstract fun usersDao(): UsersDao

}