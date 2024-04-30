package com.inter.planner.datasources

import com.inter.database.CoreDatabase
import com.inter.database.entities.LocalPlace
import com.inter.entity.DomainDbMapper
import com.inter.entity.planner.PlaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class PlaceLocalDatasource @Inject constructor(
    val coreDatabase: CoreDatabase,
    val mapper: DomainDbMapper<PlaceEntity, LocalPlace>
) {
    suspend fun createPlace(placeEntity: PlaceEntity): PlaceEntity {
        val localPlace = mapper.toDatabase(placeEntity)
       val tmp =coreDatabase.createPlace(localPlace)
        return placeEntity
    }

    suspend fun deletePlace(placeId:String):String
    {
        coreDatabase.deletePlace(placeId)
        return placeId
    }
}