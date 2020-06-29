package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.models.HousingEstateAgent
import com.openclassrooms.realestatemanager.models.HousingPoi
import com.openclassrooms.realestatemanager.repositories.*
import java.util.concurrent.Executor

class AddHousingViewModel(private val housingRepository: HousingRepository,
                          private val addressRepository: AddressRepository,
                          private val photoRepository: PhotoRepository,
                          private val housingEstateAgentRepository: HousingEstateAgentRepository,
                          private val housingPoiRepository: HousingPoiRepository)
                            : ViewModel()
{

    /*suspend fun createHousing(housing : Housing)
    {
        this.housingRepository.createHousing(housing)
        housing.getAddress()?.let { this.addressRepository.createAddress(housing.getAddress()!!) }
        housing.getPhotoList()?.let {
                for (i in it)
                {
                    this.photoRepository.createPhoto(i)
                }
            }

         housing.getEstateAgentList()?.let {
                for (i in it)
                {
                    val temp = HousingEstateAgent(i.housingReference, i.estateAgentName, i.function)
                    this.housingEstateAgentRepository.createHousingEstateAgent(temp)
                }
            }

         housing.getPoiList()?.let {
                for (i in it)
                {
                    val temp = HousingPoi(i.housingReference, i.poiType, i.numberOfPoi)
                    this.housingPoiRepository.createHousingPoi(temp)
                }
            }

    }

    suspend fun updateHousing(housing: Housing)
    {
         this.housingRepository.updateHousing(housing)
        if (housing.getAddress() != this.addressRepository.getAddressFromHousing(housing.ref).value) //TODO-Q : value : OK ?
        {
            housing.getAddress()?.let { this.addressRepository.updateAddress(housing.getAddress()!!) }
        }
         housing.getPhotoList()?.let {
            for (i in it)
            {
                this.photoRepository.createPhoto(i)
            }

        }
         housing.getEstateAgentList()?.let {
            for (i in it)
            {
                val temp = HousingEstateAgent(i.housingReference, i.estateAgentName, i.function)
                this.housingEstateAgentRepository.createHousingEstateAgent(temp)
            }

        }
         housing.getPoiList()?.let {
            for (i in it)
            {
                val temp = HousingPoi(i.housingReference, i.poiType, i.numberOfPoi)
                this.housingPoiRepository.createHousingPoi(temp)
            }
        }

    }*/

}