package com.shorman.mapsclustersample.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)