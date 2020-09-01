package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.repositories.EstateAgentRepository
import com.openclassrooms.realestatemanager.repositories.HousingRepository

class FilterViewModel (private val housingRepository: HousingRepository,
                       private val estateAgentRepository: EstateAgentRepository) : ViewModel()
{
    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()
    fun getEstateAgentList() : LiveData<List<EstateAgent>> = this.estateAgentRepository.getAllEstateAgent()

    fun getListFilter(type : String? = null,
                      priceLower : Double? = null,
                      priceHigher : Double? = null,
                      areaLower : Double? = null,
                      areaHigher : Double? = null,
                      roomLower : Int? = null,
                      roomHigher : Int? = null,
                      bedRoomLower : Int? = null,
                      bedRoomHigher : Int? = null,
                      bathRoomLower : Int? = null,
                      bathRoomHigher : Int? = null,
                      state : String? = null,
                      dateEntry : String? = null,
                      dateSale : String? = null,
                      city : String? = null,
                      country : String? = null,
                      typePoi : String? = null,
                      numberPhotos : Int? = null,
                      estateAgent : String? = null): LiveData<List<CompleteHousing>>
    {
        return this.housingRepository.getListFilter(type,
                priceLower, priceHigher, areaLower, areaHigher,
                roomLower, roomHigher, bedRoomLower, bedRoomHigher,
                bathRoomLower, bathRoomHigher, state, dateEntry,
                dateSale, city, country, typePoi, numberPhotos, estateAgent)
    }

    fun testQuery(priceLower: Double?, priceHigher: Double?, type: String?) = this.housingRepository.testQuery(priceLower, priceHigher, type)

    /*fun testSupportSQLiteQuery(query : SupportSQLiteQuery) = this.housingRepository.testSupportSQLiteQuery(query)*/
}