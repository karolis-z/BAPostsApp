package com.example.bapostsapp.core.di

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
}