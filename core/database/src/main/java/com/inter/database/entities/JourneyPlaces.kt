package com.inter.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class JourneyPlaces(
 @Embedded val journey:LocalJourney,
 @Relation(
  parentColumn ="journey_id",
  entityColumn = "ref_journey_id"
 )
 val places:List<LocalPlace>
)
