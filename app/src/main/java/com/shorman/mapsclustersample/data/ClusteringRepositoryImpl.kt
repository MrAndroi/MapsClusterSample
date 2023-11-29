package com.shorman.mapsclustersample.data

import com.shorman.mapsclustersample.data.remote.ClusteringRemoteDataSource
import com.shorman.mapsclustersample.domain.ClusteringRepository
import com.shorman.mapsclustersample.domain.model.MarkerItemModel
import javax.inject.Inject

class ClusteringRepositoryImpl @Inject constructor(
    private val dataSource: ClusteringRemoteDataSource
): ClusteringRepository {

    override suspend fun getClusters(): List<MarkerItemModel> {
        return run { dataSource.getClusteringData() }
    }

}