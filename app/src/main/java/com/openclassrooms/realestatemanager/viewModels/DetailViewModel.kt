package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


    fun getCompleteHousing(reference : String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()

    private suspend fun deleteHousing(housing : Housing) = this.housingRepository.deleteHousing(housing)

    private suspend fun deleteAddress(address: Address) = this.addressRepository.deleteAddress(address)

    private suspend fun deleteHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.deleteHousingEstateAgent(housingEstateAgent)

    private suspend fun deletePhoto(photo: Photo) = this.photoRepository.deletePhoto(photo)

    private suspend fun deleteHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.deleteHousingPoi(housingPoi)

    fun deleteGlobal (completeHousing: CompleteHousing)
    {
        viewModelScope.launch (Dispatchers.IO)
        {
            if (completeHousing.address != null) deleteAddress(completeHousing.address!!)

            if (completeHousing.estateAgentList != null || completeHousing.estateAgentList!!.isNotEmpty())
            {
                for (i in completeHousing.estateAgentList!!)
                {
                    deleteHousingEstateAgent(i)
                }
            }

            if (completeHousing.photoList != null || completeHousing.photoList!!.isNotEmpty())
            {
                for (i in completeHousing.photoList!!)
                {
                    deletePhoto(i)
                }
            }

            if (completeHousing.poiList != null || completeHousing.poiList!!.isNotEmpty())
            {
                for (i in completeHousing.poiList!!)
                {
                    deleteHousingPoi(i)
                }
            }

            deleteHousing(completeHousing.housing)
        }
    }

}