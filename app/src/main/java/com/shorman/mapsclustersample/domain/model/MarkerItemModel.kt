package com.shorman.mapsclustersample.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkerItemModel(
    val bannerImage: String? = null,
    val bannerImages: List<String>? = null,
    val coordinates: Coordinates? = null,
    val id: String? = null,
    val markerIcon: String? = null,
    val name: String? = null,
    val shortDescription: String? = null
)