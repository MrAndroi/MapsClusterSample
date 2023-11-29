package com.shorman.mapsclustersample.data.di

import com.shorman.mapsclustersample.data.ClusteringRepositoryImpl
import com.shorman.mapsclustersample.domain.ClusteringRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ClusteringFeatureModule {

    @Binds
    @Singleton
    fun bindClusteringRepository(impl: ClusteringRepositoryImpl): ClusteringRepository
}