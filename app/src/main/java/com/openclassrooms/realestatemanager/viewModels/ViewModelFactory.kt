package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repositories.*

class ViewModelFactory (private val housingRepository: HousingRepository,
                        private val addressRepository: AddressRepository,
                        private val estateAgentRepository: EstateAgentRepository,
                        private val housingEstateAgentRepository: HousingEstateAgentRepository,
                        private val housingPoiRepository : HousingPoiRepository,
                        private val photoRepository: PhotoRepository,
                        private val poiRepository: PoiRepository)
                        : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        when {
            modelClass.isAssignableFrom(AddEstateTypeViewModel::class.java) ->
                return modelClass.cast(AddEstateTypeViewModel(estateAgentRepository, poiRepository))!!
            modelClass.isAssignableFrom(AddUpdateHousingViewModel::class.java) ->
                return modelClass.cast(AddUpdateHousingViewModel(housingRepository, addressRepository, photoRepository, housingEstateAgentRepository, housingPoiRepository))!!
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                return modelClass.cast(DetailViewModel(housingRepository, addressRepository, photoRepository, housingEstateAgentRepository, housingPoiRepository))!!
            modelClass.isAssignableFrom(ListHousingViewModel::class.java) ->
                return modelClass.cast(ListHousingViewModel(housingRepository, addressRepository, photoRepository, housingEstateAgentRepository, housingPoiRepository))!!
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}