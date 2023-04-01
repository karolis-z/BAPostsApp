package com.example.bapostsapp.core.di

import android.content.Context
import androidx.room.Room
import com.example.bapostsapp.core.Constants
import com.example.bapostsapp.data.local.PostsAppDatabase
import com.example.bapostsapp.data.local.PostsDao
import com.example.bapostsapp.data.local.UsersDao
import com.example.bapostsapp.data.remote.PostsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.POSTS_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): PostsApi = retrofit.create(PostsApi::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, PostsAppDatabase::class.java, "posts_app_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providePostsDao(db: PostsAppDatabase): PostsDao = db.postsDao()

    @Singleton
    @Provides
    fun provideUsersDao(db: PostsAppDatabase): UsersDao = db.usersDao()
}