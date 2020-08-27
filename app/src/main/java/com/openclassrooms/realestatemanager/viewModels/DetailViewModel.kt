package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.openclassrooms.realestatemanager.api.CompleteHousingHelper
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.notifications.NotificationWorker
import com.openclassrooms.realestatemanager.repositories.*
import com.openclassrooms.realestatemanager.utils.ERROR_GEOCODER_ADDRESS
import com.openclassrooms.realestatemanager.utils.UtilsKotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val listCompleteHousingLiveDate = MutableLiveData<List<CompleteHousing>>()

    fun getCompleteHousing(reference : String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()

    fun getListCompleteHousingLiveData(listRoom: List<CompleteHousing>) : MutableLiveData<List<CompleteHousing>>
    {
        this.getListCompleteHousingFromFirestore(listRoom)
        return this.listCompleteHousingLiveDate
    }

    private fun getListCompleteHousingFromFirestore(listRoom : List<CompleteHousing>)
    {
        this.housingRepository.getListCompleteHousingFromFirestore().addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if (querySnapshot != null)
            {
                val listDocumentSnapshot = querySnapshot.documents
                val listToReturn = ArrayList<CompleteHousing>()

                for (i in listRoom) listToReturn.add(i)

                for (housing in listToReturn)
                {
                    if (!housing.housing.onFirestore)
                    {
                        this.createCompleteHousingFromFirestore(housing)
                    }
                }

                for (document in listDocumentSnapshot)
                {
                    val completeHousing = document.toObject(CompleteHousing::class.java)
                    if (completeHousing != null && !listRoom.contains(completeHousing))
                    {
                        listToReturn.add(completeHousing)
                    }
                }
                listCompleteHousingLiveDate.value = listToReturn
            }
        }
    }

    private fun createCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<Void>
    {
        completeHousing.housing.onFirestore = true
        return this.housingRepository.createCompleteHousingFromFirestore(completeHousing)
    }

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

    fun createCompleteHousingFromFirestore (listCompleteHousing : List<CompleteHousing>, context: Context)
    {
        viewModelScope.launch  (Dispatchers.IO)
        {
            for (housing in listCompleteHousing)
            {
                createHousing(housing.housing)
                createAllHousingPoi(housing.poiList)
                createGlobalAddress(housing.address, context)
                createAllPhoto(housing.photoList)
                createAllHousingEstateAgent(housing.estateAgentList)
            }
        }
    }

    //////////////////// UPDATE ////////////////////

    private suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    private suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    private suspend fun updateGlobalAddress(address: Address?, completeHousing: CompleteHousing, context: Context, ref : String, isInternetAvailable: Boolean, key: String)
    {
        if (address != null)
        {
            if (completeHousing.address != null && address != completeHousing.address)
            {
                val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)
                if (location != null && location != ERROR_GEOCODER_ADDRESS) updateAddress(address)
            }
        }
        else if (completeHousing.address != null) deleteAddress(completeHousing.address!!)
    }

    private suspend fun updateAllEstateHousingEstateAgent(estateAgentList: List<HousingEstateAgent>?, completeHousing: CompleteHousing)
    {
        if (!estateAgentList.isNullOrEmpty() && !completeHousing.estateAgentList.isNullOrEmpty() && estateAgentList != completeHousing.estateAgentList)
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
    }

    private suspend fun updateAllPhoto(photoList: List<Photo>?, completeHousing: CompleteHousing)
    {
        if (!photoList.isNullOrEmpty() && !completeHousing.photoList.isNullOrEmpty())
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
            for (photo in completeHousing.photoList!!) //TODO-Q : Pourquoi besoin de l'assert ici et pas en dessous ?
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


    fun updateGlobalHousing (completeHousing: CompleteHousing, housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?, context: Context, key : String, isInternetAvailable : Boolean)
    {
        viewModelScope.launch (Dispatchers.IO)
        {

            if (housing.ref == completeHousing.housing.ref)
            {
                if (housing != completeHousing.housing)
                {
                    housing.onFirestore = false
                    updateHousing(housing)
                }
                updateGlobalAddress(address, completeHousing, context, housing.ref, isInternetAvailable, key)
                updateAllEstateHousingEstateAgent(estateAgentList, completeHousing)
                updateAllPhoto(photoList, completeHousing)

            }
        }
    }

}