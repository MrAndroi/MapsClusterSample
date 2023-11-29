package com.shorman.mapsclustersample.domain

import com.shorman.mapsclustersample.domain.model.MarkerItemModel

interface ClusteringRepository {
    suspend fun getClusters(): List<MarkerItemModel>
}