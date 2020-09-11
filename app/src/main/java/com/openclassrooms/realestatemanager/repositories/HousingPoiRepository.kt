package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.daos.HousingPoiDAO
import com.openclassrooms.realestatemanager.models.HousingPoi

/**
 * Repository of [HousingPoiDAO]
 */
class HousingPoiRepository (private val housingPoiDAO: HousingPoiDAO)
{
    suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiDAO.createHousingPoi(housingPoi)

    suspend fun deleteHousingPoi(housingPoi: HousingPoi) = this.housingPoiDAO.deleteHousingPoi(housingPoi)

}