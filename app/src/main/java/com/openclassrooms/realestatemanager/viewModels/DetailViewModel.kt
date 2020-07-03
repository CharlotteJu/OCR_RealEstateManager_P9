package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> DetailFragment
 */
class DetailViewModel constructor(private val housingRepository: HousingRepository,
                                  private val addressRepository: AddressRepository,
                                  private val photoRepository: PhotoRepository,
                                  private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                  private val housingPoiRepository: HousingPoiRepository)
                                    : ViewModel()
{


    fun getHousing(reference : String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)

    fun getGlobalHousingList() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()

}