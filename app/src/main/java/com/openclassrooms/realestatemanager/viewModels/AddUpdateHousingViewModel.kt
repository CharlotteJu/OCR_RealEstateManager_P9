package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

/**
 * View Model to add and update [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> AddHousingFragment & EditHousingFragment
 */
class AddUpdateHousingViewModel(private val housingRepository: HousingRepository,
                                private val addressRepository: AddressRepository,
                                private val photoRepository: PhotoRepository,
                                private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                private val housingPoiRepository: HousingPoiRepository,
                                private val estateAgentRepository: EstateAgentRepository)
                                : ViewModel()
{

    //////////////////// GET ////////////////////

    fun getEstateAgentList() : LiveData<List<EstateAgent>> = this.estateAgentRepository.getAllEstateAgent()

    fun getHousing(reference : String) : LiveData<Housing> = this.housingRepository.getHousing(reference)



    //////////////////// CREATE ////////////////////


    private suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    private suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    private suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    private suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    private suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    fun createGlobal (housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?)
    {
        val test1 =  viewModelScope.launch (Dispatchers.IO)
        {
            //TODO-Q : Pour chaque suspend fun ?

            createHousing(housing)

            if (address != null)
            {
                createAddress(address)
            }

            if (estateAgentList != null)
            {
                for (estate in estateAgentList)
                {
                    createHousingEstateAgent(estate)
                }
            }

            if (photoList != null)
            {
                for (photo in photoList)
                {
                    createPhoto(photo)
                }
            }
            //createHousingPoi(housingPoi) // TODO : Voir pour les POI
        }

        //test1.join()

    }


    //////////////////// UPDATE ////////////////////

    suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    suspend fun updateHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.updateHousingEstateAgent(housingEstateAgent)

    suspend fun updateHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.updateHousingPoi(housingPoi)


    /* suspend fun createGlobalHousing(housing: Housing, address: Address?, photoList: List<Photo>?, housingEstateAgentList: List<HousingEstateAgent>?, housingPoiList: List<HousingPoi>?)
    {
        this.createHousing(housing)
        this.createAddress(address!!)
        for (photo in photoList!!)
        {
            this.createPhoto(photo)
        }
        for (housingEstateAgent in housingEstateAgentList!!)
        {
            this.createHousingEstateAgent(housingEstateAgent)
        }
        for (housingPoi in housingPoiList!!)
        {
            this.createHousingPoi(housingPoi)
        }

    }*/












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

    }*/



}

