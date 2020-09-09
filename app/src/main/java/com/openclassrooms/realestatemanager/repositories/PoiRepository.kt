package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.daos.PoiDAO
import com.openclassrooms.realestatemanager.models.Poi

/**
 * Repository of [PoiDAO]
 */
class PoiRepository (private val poiDAO: PoiDAO)
{
    suspend fun createPoi(poi: Poi) = this.poiDAO.createPoi(poi)

    suspend fun deletePoi (poi: Poi) = this.poiDAO.deletePoi(poi)
}