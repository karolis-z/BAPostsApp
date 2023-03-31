package com.example.bapostsapp.core.di

import com.example.bapostsapp.core.Constants
import com.example.bapostsapp.data.remote.PostsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    fun provideMoshi(): Moshi = Moshi.Builder()
//        .add(Kotl())
//        .build()

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

}