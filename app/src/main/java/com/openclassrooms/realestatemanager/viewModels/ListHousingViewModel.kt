package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import com.openclassrooms.realestatemanager.utils.ERROR_GEOCODER_ADDRESS
import com.openclassrooms.realestatemanager.utils.UtilsKotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()

    private fun getListCompleteHousingFromFirestore(listRoom : List<CompleteHousing>, context: Context)
    {
        this.housingRepository.getListCompleteHousingFromFirestore().addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if (querySnapshot != null)
            {
                val listDocumentSnapshot = querySnapshot.documents

                for (housing in listRoom)
                {
                    if (!housing.housing.onFirestore)
                    {
                        this.createCompleteHousingOnFirestore(housing)
                    }
                }

                for (document in listDocumentSnapshot)
                {
                    val completeHousing = document.toObject(CompleteHousing::class.java)
                    if (completeHousing != null && !listRoom.contains(completeHousing))
                    {
                        this.createCompleteHousingOnRoom(completeHousing, context)
                    }
                }
            }
        }
    }

    private fun createCompleteHousingOnFirestore(completeHousing: CompleteHousing) : Task<Void>
    {
        completeHousing.housing.onFirestore = true
        return this.housingRepository.createCompleteHousingFromFirestore(completeHousing)
    }

    private suspend fun deleteAddress(address: Address) = this.addressRepository.deleteAddress(address)

    private suspend fun deleteHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.deleteHousingEstateAgent(housingEstateAgent)

    private suspend fun deletePhoto(photo: Photo) = this.photoRepository.deletePhoto(photo)

    private suspend fun deleteHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.deleteHousingPoi(housingPoi)

    //////////////////// CREATE ////////////////////

    private suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    private suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    private suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    private suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    private suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    private suspend fun createGlobalAddress(address: Address?, context: Context)
    {
        if (address != null)
        {
            val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)
            if (location != null && location != ERROR_GEOCODER_ADDRESS) createAddress(address)
        }
    }

    private suspend fun createAllHousingEstateAgent(estateAgentList: List<HousingEstateAgent>?)
    {
        if (!estateAgentList.isNullOrEmpty())
        {
            for (estate in estateAgentList)
            {
                createHousingEstateAgent(estate)
            }
        }
    }

    private suspend fun createAllPhoto(photoList: List<Photo>?)
    {
        if (!photoList.isNullOrEmpty())
        {
            for (photo in photoList)
            {
                createPhoto(photo)
            }
        }
    }

    private suspend fun createAllHousingPoi(poiList : List<HousingPoi>?)
    {
        if (!poiList.isNullOrEmpty())
        {
            for (poi in poiList)
            {
                createHousingPoi(poi)
            }
        }
    }

    private fun createCompleteHousingOnRoom (completeHousing :CompleteHousing, context: Context)
    {
        viewModelScope.launch  (Dispatchers.IO)
        {
            createHousing(completeHousing.housing)
            createAllHousingPoi(completeHousing.poiList)
            createGlobalAddress(completeHousing.address, context)
            createAllPhoto(completeHousing.photoList)
            createAllHousingEstateAgent(completeHousing.estateAgentList)
        }
    }

    //////////////////// UPDATE ////////////////////

    private suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    private suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    private suspend fun updateGlobalAddress(address: Address?, completeHousing: CompleteHousing, context: Context)
    {
        if (address != null)
        {
            if (completeHousing.address != null && address != completeHousing.address)
            {
                val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)
                if (location != null && location != ERROR_GEOCODER_ADDRESS) updateAddress(address)
            }
            else if (completeHousing.address == null) createAddress(address)
        }
        else if (completeHousing.address != null) deleteAddress(completeHousing.address!!)
    }

    private suspend fun updateAllEstateHousingEstateAgent(estateAgentList: List<HousingEstateAgent>?, completeHousing: CompleteHousing)
    {
        if (estateAgentList != null && completeHousing.estateAgentList != null && estateAgentList != completeHousing.estateAgentList)
        {
            for (i in estateAgentList)
            {
                if (!completeHousing.estateAgentList!!.contains(i)) createHousingEstateAgent(i)
            }
            for (i in completeHousing.estateAgentList!!)
            {
                if (!estateAgentList.contains(i)) deleteHousingEstateAgent(i)
            }
        }
        else if (estateAgentList == null && !completeHousing.estateAgentList.isNullOrEmpty())
        {
            for (i in completeHousing.estateAgentList!!)
            {
                deleteHousingEstateAgent(i)
            }
        }
        else if (completeHousing.estateAgentList == null && !estateAgentList.isNullOrEmpty())
        {
            for (i in estateAgentList)
            {
                createHousingEstateAgent(i)
            }
        }
    }

    private suspend fun updateAllPhoto(photoList: List<Photo>?, completeHousing: CompleteHousing)
    {
        if (photoList != null && completeHousing.photoList != null)
        {
            for (i in photoList)
            {
                if (!completeHousing.photoList!!.contains(i)) createPhoto(i)
                else
                {
                    val index = completeHousing.photoList!!.indexOf(i)
                    if (completeHousing.photoList!![index].description != i.description) updatePhoto(i)
                }
            }
            for (i in completeHousing.photoList!!)
            {
                if (!photoList.contains(i)) deletePhoto(i)
            }
        }
        else if (photoList == null && !completeHousing.photoList.isNullOrEmpty())
        {
            for (photo in completeHousing.photoList!!)
            {
                deletePhoto(photo)
            }
        }
        else if (!photoList.isNullOrEmpty() && completeHousing.photoList == null)
        {
            for (photo in photoList)
            {
                createPhoto(photo)
            }
        }
    }

    private suspend fun updateAllHousingPoi(listPoi : List<HousingPoi>?, completeHousing: CompleteHousing)
    {
        if (listPoi != null && completeHousing.poiList != null && listPoi != completeHousing.estateAgentList)
        {
            for (i in listPoi)
            {
                if (!completeHousing.poiList!!.contains(i)) createHousingPoi(i)
            }
            for (i in completeHousing.poiList!!)
            {
                if (!listPoi.contains(i)) deleteHousingPoi(i)
            }
        }
        else if (listPoi == null && !completeHousing.poiList.isNullOrEmpty())
        {
            for (i in completeHousing.poiList!!)
            {
                deleteHousingPoi(i)
            }
        }
        else if (completeHousing.poiList == null && !listPoi.isNullOrEmpty())
        {
            for (i in listPoi)
            {
                createHousingPoi(i)
            }
        }
    }


    fun updateGlobalHousing (listHousingFromRoom : List<CompleteHousing>, listHousingFromFirestore : List<CompleteHousing>, context: Context) //TODO : Où appeler ? 
    {
        viewModelScope.launch (Dispatchers.IO)
        {

            for (housing in listHousingFromFirestore)
            {
                val index : Int
                if (listHousingFromRoom.contains(housing))
                {
                    index = listHousingFromRoom.indexOf(housing)
                    if (housing != listHousingFromFirestore[index])
                    {
                        if (housing.housing != listHousingFromFirestore[index].housing) updateHousing(housing.housing)
                        if (housing.address != listHousingFromFirestore[index].address)
                        {
                            updateGlobalAddress(housing.address, listHousingFromFirestore[index], context)
                        }
                        if (housing.photoList != listHousingFromFirestore[index].photoList)
                        {
                            updateAllPhoto(housing.photoList, listHousingFromFirestore[index])
                        }
                        if (housing.estateAgentList != listHousingFromFirestore[index].estateAgentList)
                        {
                            updateAllEstateHousingEstateAgent(housing.estateAgentList, listHousingFromFirestore[index])
                        }
                        if (housing.poiList != listHousingFromFirestore[index].poiList)
                        {
                            updateAllHousingPoi(housing.poiList, listHousingFromFirestore[index])
                        }
                    }
                }
            }
        }
    }
}


