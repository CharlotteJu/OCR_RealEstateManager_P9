package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.EstateAgentRepository
import com.openclassrooms.realestatemanager.repositories.HousingRepository

/**
 * View Model to get [CompleteHousing], [EstateAgent] --> DetailFragment
 */
class FilterViewModel (private val housingRepository: HousingRepository,
                       private val estateAgentRepository: EstateAgentRepository) : ViewModel()
{
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
                      dateEntry : Long? = null,
                      dateSale : Long? = null,
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


}