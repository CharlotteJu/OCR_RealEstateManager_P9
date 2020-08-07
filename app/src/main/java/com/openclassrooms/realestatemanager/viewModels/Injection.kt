package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.repositories.*

/**
 * Using if we don't use Koin
 */
class Injection
{

    companion object {

        private fun getHousingDao(context: Context) = AppDatabase.getDatabase(context).housingDao()
        private fun getAddressDao(context: Context) = AppDatabase.getDatabase(context).addressDao()
        private fun getEstateAgentDao(context: Context) = AppDatabase.getDatabase(context).estateAgentDao()
        private fun getPhotoDao(context: Context) = AppDatabase.getDatabase(context).photoDao()
        private fun getPoiDao(context: Context) = AppDatabase.getDatabase(context).poiDao()
        private fun getHousingEstateAgentDao(context: Context) = AppDatabase.getDatabase(context).housingEstateAgentDao()
        private fun getHousingPoiDao(context: Context) = AppDatabase.getDatabase(context).housingPoiDao()

        fun configViewModelFactory(context: Context) : ViewModelFactory{
            val housingRepository =  HousingRepository(getHousingDao(context))
            val addressRepository = AddressRepository(getAddressDao(context))
            val estateAgentRepository = EstateAgentRepository(getEstateAgentDao(context))
            val photoRepository = PhotoRepository(getPhotoDao(context))
            val poiRepository = PoiRepository(getPoiDao(context))
            val housingEstateAgentRepository = HousingEstateAgentRepository(getHousingEstateAgentDao(context))
            val housingPoiRepository = HousingPoiRepository(getHousingPoiDao(context))

            return ViewModelFactory(housingRepository, addressRepository, estateAgentRepository, housingEstateAgentRepository, housingPoiRepository, photoRepository, poiRepository)
        }
    }
}