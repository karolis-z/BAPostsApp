package com.example.bapostsapp.core.di

import com.example.bapostsapp.core.util.ConnectivityChecker
import com.example.bapostsapp.core.util.ConnectivityCheckerImpl
import com.example.bapostsapp.data.local.PostsLocalDataSource
import com.example.bapostsapp.data.local.PostsLocalDataSourceImpl
import com.example.bapostsapp.data.local.UsersLocalDataSource
import com.example.bapostsapp.data.local.UsersLocalDataSourceImpl
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

    @Binds
    abstract fun bindConnectivityChecker(
        connectivityCheckerImpl: ConnectivityCheckerImpl
    ): ConnectivityChecker

    @Binds
    abstract fun bindPostsLocalDataSource(
        postsLocalDataSourceImpl: PostsLocalDataSourceImpl
    ): PostsLocalDataSource

    @Binds
    abstract fun bindUsersLocalDataSource(
        usersLocalDataSourceImpl: UsersLocalDataSourceImpl
    ): UsersLocalDataSource
}