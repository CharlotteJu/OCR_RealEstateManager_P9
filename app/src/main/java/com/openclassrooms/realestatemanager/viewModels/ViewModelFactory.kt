package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repositories.*

/**
 * Using if we don't use Koin
 */
class ViewModelFactory (private val housingRepository: HousingRepository,
                        private val addressRepository: AddressRepository,
                        private val estateAgentRepository: EstateAgentRepository,
                        private val housingEstateAgentRepository: HousingEstateAgentRepository,
                        private val housingPoiRepository : HousingPoiRepository,
                        private val photoRepository: PhotoRepository,
                        private val poiRepository: PoiRepository,
                        private val placesPoiRepository: PlacesPoiRepository)
                        : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T
    {
        when {
            modelClass.isAssignableFrom(AddEstateAgentViewModel::class.java) ->
                return modelClass.cast(AddEstateAgentViewModel(estateAgentRepository, poiRepository))!!
            modelClass.isAssignableFrom(AddUpdateHousingViewModel::class.java) ->
                return modelClass.cast(AddUpdateHousingViewModel(housingRepository, addressRepository, photoRepository, housingEstateAgentRepository, housingPoiRepository, estateAgentRepository, placesPoiRepository))!!
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                return modelClass.cast(DetailViewModel(housingRepository))!!
            modelClass.isAssignableFrom(FilterViewModel::class.java) ->
                return modelClass.cast(FilterViewModel(housingRepository, estateAgentRepository))!!
            modelClass.isAssignableFrom(ListHousingViewModel::class.java) ->
                return modelClass.cast(ListHousingViewModel(housingRepository, addressRepository, photoRepository, housingEstateAgentRepository,housingPoiRepository, estateAgentRepository))!!
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}