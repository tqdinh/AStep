package com.example.home.utils

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object CalculateDistance {

    val EARTH_RADIUS = 6371

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        // Convert latitude and longitude from degrees to radians
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // Calculate the differences between coordinates
        val latDiff = lat2Rad - lat1Rad
        val lonDiff = lon2Rad - lon1Rad

        // Calculate the distance using the Haversine formula
        val a =
            sin(latDiff / 2) * sin(latDiff / 2) + cos(lat1Rad) * cos(lat2Rad) * sin(lonDiff / 2) * sin(
                lonDiff / 2
            )
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS * c
    }

    fun GetDistance(list: List<Pair<Double, Double>>): String {
        val l0 = list.subList(0, list.size - 1);
        val l1 = list.subList(1, list.size)
        val result = l0.zip(l1) { a, b ->
            calculateDistance(a.first, a.second, b.first, b.second)
        }
        val total = result.sum()
        return total.toString()

    }
}