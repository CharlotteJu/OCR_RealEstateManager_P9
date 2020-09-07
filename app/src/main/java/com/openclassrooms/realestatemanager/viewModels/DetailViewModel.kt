package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> DetailFragment
 */
class DetailViewModel constructor(private val housingRepository: HousingRepository)
                                    : ViewModel()
{
    fun getCompleteHousing(reference : String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)
    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()
}