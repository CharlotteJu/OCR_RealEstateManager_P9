package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import java.util.concurrent.Executor

/**
 * View Model to add and update [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> AddHousingFragment & EditHousingFragment
 */
class AddUpdateHousingViewModel(private val housingRepository: HousingRepository,
                                private val addressRepository: AddressRepository,
                                private val photoRepository: PhotoRepository,
                                private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                private val housingPoiRepository: HousingPoiRepository)
                                : ViewModel()
{

    //////////////////// CREATE ////////////////////



    suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)


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

   /* suspend fun updateGlobalHousing(housing: Housing, address: Address, photoList: List<Photo>?, housingEstateAgentList: List<HousingEstateAgent>?, housingPoiList: List<HousingPoi>?)
    {
        val updateHousing = this.housingRepository.getHousing(housing.ref).value

        if (housing != updateHousing)
        {
            this.updateHousing(housing)
        }

        if (address != updateHousing!!.address)
        {
            this.updateAddress(address)
        }

        for (i in photoList!!)
        {
            for (j in updateHousing.photoList!!)
            {
                if (i.uri == j.uri && i != j)
                {
                    this.updatePhoto(i)
                }
            }
            if(!updateHousing.photoList!!.contains(i))
            {
                this.createPhoto(i)
            }
        }

        for (i in housingPoiList!!)
        {
            for (j in updateHousing.poiList!!)
            {
                if (i.poiType == j.poiType && i != j)
                {
                    this.updateHousingPoi(i)
                }
                if (!updateHousing.poiList!!.contains(i))
                {
                    this.createHousingPoi(i)
                }
            }
        }*/
        // TODO : Si 2 fois le même agent mais fonctions différentes ?
        /*for (i in housingEstateAgentList!!)
        {
            for (j in updateHousing.estateAgentList!!)
            {
                if (i.)
            }
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