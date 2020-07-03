package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> ListFragment & MapFragment
 */
class ListHousingViewModel(private val housingRepository: HousingRepository,
                           private val addressRepository: AddressRepository,
                           private val photoRepository: PhotoRepository,
                           private val housingEstateAgentRepository: HousingEstateAgentRepository,
                           private val housingPoiRepository: HousingPoiRepository)
                            : ViewModel()
{


    fun getHousingList() : LiveData<List<Housing>> = this.housingRepository.getAllHousing()

    fun getAddressList() : LiveData<List<Address>> = this.addressRepository.getAllAddress()

    fun getAllPhotoList() : LiveData<List<Photo>> = this.photoRepository.getAllPhoto()

    fun getAllHousingEstateAgentList() : LiveData<List<HousingEstateAgent>> = this.housingEstateAgentRepository.getAllHousingEstateAgent()

    fun getAllHousingPoiList() : LiveData<List<HousingPoi>> = this.housingPoiRepository.getAllHousingPoi()


}