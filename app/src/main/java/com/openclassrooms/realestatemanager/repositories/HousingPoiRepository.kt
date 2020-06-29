package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.daos.HousingPoiDAO
import com.openclassrooms.realestatemanager.models.HousingPoi

class HousingPoiRepository (private val housingPoiDAO: HousingPoiDAO)
{
    fun getAllHousingPoi() : LiveData<List<HousingPoi>> = this.housingPoiDAO.getAllHousingPoi()

    fun getHousingPoi(reference: String, type: String) : LiveData<HousingPoi> = this.housingPoiDAO.getHousingPoi(reference, type)

    fun getHousingPoiFromHousing(reference: String) : LiveData<List<HousingPoi>> = this.housingPoiDAO.getHousingPoiListFromHousing(reference)

    fun getHousingPoiFromPoi(type: String) : LiveData<List<HousingPoi>> = this.housingPoiDAO.getHousingPoiListFromPoi(type)

    suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiDAO.createHousingPoi(housingPoi)

    suspend fun updateHousingPoi(housingPoi: HousingPoi) = this.housingPoiDAO.updateHousingPoi(housingPoi)

}