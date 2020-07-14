package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.daos.PoiDAO
import com.openclassrooms.realestatemanager.models.Photo
import com.openclassrooms.realestatemanager.models.Poi

class PoiRepository (private val poiDAO: PoiDAO)
{
    fun getAllPoi() : LiveData<List<Poi>> = this.poiDAO.getAllPoi()

    fun getPoi(type : String) : LiveData<Poi> = this.poiDAO.getPoi(type)

    suspend fun createPoi(poi: Poi) = this.poiDAO.createPoi(poi)

    suspend fun updatePoi(poi: Poi) = this.poiDAO.updatePoi(poi)

    suspend fun deletePoi (poi: Poi) = this.poiDAO.deletePoi(poi)

}