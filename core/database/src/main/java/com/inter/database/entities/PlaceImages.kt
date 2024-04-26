package com.inter.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PlaceImages(
    @Embedded
    val place: LocalPlace,
    @Relation(
        parentColumn = "place_id",
        entityColumn = "ref_place_id"
    )
    val images: List<LocalImage>


)