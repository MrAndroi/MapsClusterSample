package com.shorman.mapsclustersample.data.di

import android.content.Context
import com.serjltt.moshi.adapters.Wrapped
import com.shorman.mapsclustersample.BuildConfig
import com.shorman.mapsclustersample.data.interceptors.ApiKeyInterceptor
import com.shorman.mapsclustersample.data.remote.ClusteringRemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object ClusteringRemoteModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(factory: Converter.Factory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(moshi: Moshi): Converter.Factory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Wrapped.ADAPTER_FACTORY)
            .build()
    }

    @Provides
    @Singleton
    fun provideClusteringRemoteDataSource(retrofit: Retrofit): ClusteringRemoteDataSource {
        return retrofit.create()
    }
}