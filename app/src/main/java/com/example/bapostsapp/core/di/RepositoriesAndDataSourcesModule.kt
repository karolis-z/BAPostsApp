package com.example.bapostsapp.core.di

import com.example.bapostsapp.data.remote.PostsRemoteDataSource
import com.example.bapostsapp.data.remote.PostsRemoteDataSourceImpl
import com.example.bapostsapp.data.remote.UserRemoteDataSourceImpl
import com.example.bapostsapp.data.remote.UsersRemoteDataSource
import com.example.bapostsapp.data.repositories.PostsRepositoryImpl
import com.example.bapostsapp.domain.repositories.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesAndDataSourcesModule {

    @Binds
    abstract fun bindPostsRepository(postsRepositoryImpl: PostsRepositoryImpl): PostsRepository

    @Binds
    abstract fun bindPostsRemoteDataSource(
        postsRemoteDataSourceImpl: PostsRemoteDataSourceImpl
    ): PostsRemoteDataSource

    @Binds
    abstract fun bindUsersRemoteDataSource(
        userRemoteDataSourceImpl: UserRemoteDataSourceImpl
    ): UsersRemoteDataSource
}