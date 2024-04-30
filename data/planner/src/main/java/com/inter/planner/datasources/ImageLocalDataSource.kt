package com.inter.planner.datasources

import com.inter.database.CoreDatabase
import com.inter.database.entities.LocalImage
import com.inter.database.entities.LocalPlace
import com.inter.entity.DomainDbMapper
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.PlaceEntity
import javax.inject.Inject

class ImageLocalDataSource @Inject constructor(
    val coreDatabase: CoreDatabase,
    val mapper: DomainDbMapper<ImageEntity, LocalImage>
) {
    suspend fun createImage(imageEntity: ImageEntity): ImageEntity {
        val localImage = mapper.toDatabase(imageEntity)
        val tmp = coreDatabase.createImage(localImage)
        return imageEntity
    }

    suspend fun deletImage(imageId: String): String {
        coreDatabase.deleteImage(imageId)
        return imageId
    }
}