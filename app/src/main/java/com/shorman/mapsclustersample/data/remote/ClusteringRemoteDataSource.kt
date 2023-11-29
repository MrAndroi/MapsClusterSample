package com.shorman.mapsclustersample.data.remote

import com.serjltt.moshi.adapters.Wrapped
import com.shorman.mapsclustersample.domain.model.MarkerItemModel
import retrofit2.http.GET

interface ClusteringRemoteDataSource {

    @GET("pointOfInterests")
    @Wrapped(path = ["response"])
    suspend fun getClusteringData(): List<MarkerItemModel>
}