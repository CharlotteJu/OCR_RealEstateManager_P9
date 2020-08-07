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

    fun getCompleteHousing(reference: String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)



    //////////////////// CREATE ////////////////////


    private suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    private suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    private suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    private suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    private suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    fun createGlobalHousing (housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?, poiList : List<HousingPoi>? )
    {
        viewModelScope.launch (Dispatchers.IO)
        {
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

            if (poiList != null)
            {
                for (poi in poiList)
                {
                    createHousingPoi(poi)
                }
            }

            // Appeler POI ICI
        }

        //test1.join()

    }


    //////////////////// UPDATE ////////////////////

    private suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    private suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    private suspend fun deletePhoto(photo: Photo) = this.photoRepository.deletePhoto(photo)

    private suspend fun updateHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.updateHousingEstateAgent(housingEstateAgent)

    private suspend fun deleteHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.deleteHousingEstateAgent(housingEstateAgent)

    private suspend fun updateHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.updateHousingPoi(housingPoi)

    fun updateGlobalHousing (completeHousing: CompleteHousing, housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?)
    {
        viewModelScope.launch (Dispatchers.IO)
        {

            if (housing.ref == completeHousing.housing.ref)
            {
                if (housing != completeHousing.housing) updateHousing(housing)
                if (address != null && completeHousing.address != null && address != completeHousing.address) updateAddress(address)

                if (estateAgentList != null && completeHousing.estateAgentList != null && estateAgentList != completeHousing.estateAgentList)
                {
                    for (i in estateAgentList)
                    {
                        if (!completeHousing.estateAgentList?.contains(i)!!) createHousingEstateAgent(i) // TODO-Q : Ok ?
                    }

                    for (i in completeHousing.estateAgentList!!)
                    {
                        if (!estateAgentList.contains(i)) deleteHousingEstateAgent(i)
                    }
                }

                if (photoList != null && completeHousing.photoList != null && photoList != completeHousing.photoList)
                {
                    for (i in photoList)
                    {
                        if (!completeHousing.photoList?.contains(i)!!) createPhoto(i)
                        else
                        {
                            val index = completeHousing.photoList!!.indexOf(i)
                            if (completeHousing.photoList!![index] != i) updatePhoto(i) // TODO-Q : Ok ?
                        }
                    }

                    for (i in completeHousing.photoList!!)
                    {
                        if (!photoList.contains(i)) deletePhoto(i) // TODO-Q : Si un attribut est différent, ça va supprimer quand même ?
                    }
                }
            }

            // TODO : Voir pour les POI
        }

    }











}


