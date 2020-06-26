package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.repositories.*
import java.util.concurrent.Executor

class DetailViewModel @JvmOverloads constructor(private val housingRepository: HousingRepository,
                                                private val addressRepository: AddressRepository,
                                                private val estateAgentRepository: EstateAgentRepository,
                                                private val photoRepository: PhotoRepository,
                                                private val poiRepository: PoiRepository,
                                                private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                                private val housingPoiRepository: HousingPoiRepository,
                                                private val executor: Executor) : ViewModel()
{
    fun getHousing(reference : String) : LiveData<Housing>
    {
       return this.housingRepository.getHousing(reference)
    }
}