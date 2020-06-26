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
                          private val housingPoiRepository: HousingPoiRepository,
                          private val executor: Executor) : ViewModel()
{

    fun createHousing(housing : Housing)
    {
        this.executor.execute {this.housingRepository.createHousing(housing)}
        this.executor.execute { housing.address?.let { this.addressRepository.createAddress(housing.address!!) } }
        this.executor.execute { housing.photoList?.let {
                for (i in it)
                {
                    this.photoRepository.createPhoto(i)
                }
            }
        }
        this.executor.execute { housing.estateAgentList?.let {
                for (i in it)
                {
                    val temp = HousingEstateAgent(i.housingReference, i.estateAgentName, i.function)
                    this.housingEstateAgentRepository.createHousingEstateAgent(temp)
                }
            }
        }
        this.executor.execute { housing.poiList?.let {
                for (i in it)
                {
                    val temp = HousingPoi(i.housingReference, i.poiType, i.numberOfPoi)
                    this.housingPoiRepository.createHousingPoi(temp)
                }
            }
        }
    }

    fun updateHousing(housing: Housing)
    {
        this.executor.execute { this.housingRepository.updateHousing(housing) }
        if (housing.address != this.addressRepository.getAddressFromHousing(housing.ref).value) //TODO-Q : value : OK ?
        {
            this.executor.execute { housing.address?.let { this.addressRepository.updateAddress(housing.address!!) } }
        }
        this.executor.execute { housing.photoList?.let {
            for (i in it)
            {
                this.photoRepository.createPhoto(i)
            }
        }
        }
        this.executor.execute { housing.estateAgentList?.let {
            for (i in it)
            {
                val temp = HousingEstateAgent(i.housingReference, i.estateAgentName, i.function)
                this.housingEstateAgentRepository.createHousingEstateAgent(temp)
            }
        }
        }
        this.executor.execute { housing.poiList?.let {
            for (i in it)
            {
                val temp = HousingPoi(i.housingReference, i.poiType, i.numberOfPoi)
                this.housingPoiRepository.createHousingPoi(temp)
            }
        }
        }
    }

}