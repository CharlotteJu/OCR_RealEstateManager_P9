package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.repositories.*
import java.util.concurrent.Executor

class DetailViewModel constructor(private val housingRepository: HousingRepository,
                                                private val addressRepository: AddressRepository,
                                                private val estateAgentRepository: EstateAgentRepository,
                                                private val photoRepository: PhotoRepository,
                                                private val poiRepository: PoiRepository,
                                                private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                                private val housingPoiRepository: HousingPoiRepository,
                                                private val executor: Executor) : ViewModel() //TODO : Remplacer Executor par Coroutines
{
    fun getHousing(reference : String) : LiveData<Housing>
    {
        var housing = this.housingRepository.getHousing(reference)
        //var function : Function = setOf(Function)

        //var test = Transformations.map(housing, function )
        //var address = this.addressRepository.getAddressFromHousing(reference)
        //var photoList = this.photoRepository.getPhotoListFromHousing(reference)


        return housing
    }
}