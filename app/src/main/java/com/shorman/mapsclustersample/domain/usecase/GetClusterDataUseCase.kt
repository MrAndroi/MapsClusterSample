package com.shorman.mapsclustersample.domain.usecase

import com.shorman.mapsclustersample.domain.ClusteringRepository
import com.shorman.mapsclustersample.domain.model.MarkerItemModel
import javax.inject.Inject

class GetClusterDataUseCase @Inject constructor(
    private val repository: ClusteringRepository
) {

    suspend operator fun invoke(): List<MarkerItemModel> {
        return repository.getClusters()
    }
}