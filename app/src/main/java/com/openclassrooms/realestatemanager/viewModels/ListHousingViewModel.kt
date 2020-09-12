package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi], [CompleteHousing] --> ListFragment & MapFragment
 */
class ListHousingViewModel(private val housingRepository: HousingRepository,
                           private val addressRepository: AddressRepository,
                           private val photoRepository: PhotoRepository,
                           private val housingEstateAgentRepository: HousingEstateAgentRepository,
                           private val housingPoiRepository: HousingPoiRepository,
                           private val estateAgentRepository: EstateAgentRepository)
                            : ViewModel()
{

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingRepository.getAllCompleteHousing()

    fun getAllEstateAgent() : LiveData<List<EstateAgent>> = this.estateAgentRepository.getAllEstateAgent()

    /**
     * Sync data with Firebase for EstateAgent
     */
    fun syncEstateAgentWithFirebase(listRoom: List<EstateAgent>?)
    {
        viewModelScope.launch {
            //Get the EstateAgent's list from Firebase
            val query = estateAgentRepository.getEstateAgentListFromFirestore()
            if (query != null)
            {
                val listDocumentSnapshot = query.documents
                val listFirestore = ArrayList<EstateAgent>()

                for (document in listDocumentSnapshot) {
                    val estateAgent = document.toObject(EstateAgent::class.java)
                    if (estateAgent != null) {
                        listFirestore.add(estateAgent)
                    }
                }

                //Check the differences between EstateAgent's list Room and Firebase
                if(!listRoom.isNullOrEmpty())
                {
                    for (eRoom in listRoom) {
                        if (!listFirestore.contains(eRoom)) {
                            createEstateAgentInFirebase(eRoom)
                        } else {
                            val index = listFirestore.indexOf(eRoom)
                            if (eRoom.lastUpdateEstate > listFirestore[index].lastUpdateEstate) {
                                createEstateAgentInFirebase(eRoom)
                            } else if (eRoom.lastUpdateEstate < listFirestore[index].lastUpdateEstate) {
                                createEstateAgentInRoom(listFirestore[index])
                            }
                        }
                    }
                }

                //We have to check on the other side if listRoom is Empty
                for (eFirestore in listFirestore) {
                    if (listRoom.isNullOrEmpty()  || !listRoom.contains(eFirestore)) {
                        createEstateAgentInRoom(eFirestore)
                    }
                }

            }
        }
    }

    /**
     *
     */
    fun syncCompleteHousingWithFirebase(listRoom : List<CompleteHousing>?) {
        viewModelScope.launch {
            val querySnapshot = housingRepository.getCompleteHousingListFromFirestore()
            if (querySnapshot != null) {
                val listDocumentSnapshot = querySnapshot.documents
                val listFirestore = ArrayList<CompleteHousing>()

                for (document in listDocumentSnapshot) {
                    val completeHousing = document.toObject(CompleteHousing::class.java)
                    if (completeHousing != null) {
                        listFirestore.add(completeHousing)
                    }
                }

                if (!listRoom.isNullOrEmpty())
                {
                    for (hRoom in listRoom) {
                        if (!listFirestore.contains(hRoom)) {
                            createCompleteHousingOnFirestore(hRoom)
                        } else {
                            val index = listFirestore.indexOf(hRoom)
                            if (hRoom.housing.lastUpdate > listFirestore[index].housing.lastUpdate) {
                                createCompleteHousingOnFirestore(hRoom)
                            } else if (hRoom.housing.lastUpdate < listFirestore[index].housing.lastUpdate) {
                                createCompleteHousingOnRoom(listFirestore[index])
                            }
                        }
                    }
                }

                for (hFirestore in listFirestore) {
                    if (listRoom.isNullOrEmpty()  || !listRoom.contains(hFirestore)) {
                        createCompleteHousingOnRoom(hFirestore)
                    }
                }

            }
        }
    }




    //////////////////// CREATE ////////////////////

    private suspend fun createCompleteHousingOnFirestore(completeHousing: CompleteHousing)
    {
        viewModelScope.launch {
            if (!completeHousing.photoList.isNullOrEmpty())
            {
                for (photo in completeHousing.photoList!!)
                {
                    val thread = async { uploadAPhotoInFirestore(photo) }
                    thread.await()
                }
            }
            housingRepository.createCompleteHousingInFirestore(completeHousing).await()
        }

    }

    /**
     * Upload a [Photo] in FirebaseStorage and update it in Room (photo.url_firebase)
     */
    private suspend fun uploadAPhotoInFirestore(photo : Photo)
    {
        if (photo.url_firebase == null)
        {
            val task = housingRepository.pushPhotoOnFirebaseStorage(photo)
            if (task != null)
            {
                val uri = task.storage.downloadUrl.await()
                photo.url_firebase = uri.toString()
                updatePhoto(photo)
            }
        }
    }

    private suspend fun createCompleteHousingOnRoom (completeHousing :CompleteHousing)
    {
        createHousing(completeHousing.housing)
        createAllHousingPoi(completeHousing.poiList)
        createGlobalAddress(completeHousing.address)
        createAllPhoto(completeHousing.photoList)
        createAllHousingEstateAgent(completeHousing.estateAgentList)
    }

    private suspend fun createEstateAgentInRoom(estateAgent: EstateAgent) = this.estateAgentRepository.createEstateAgent(estateAgent)
    private suspend fun createEstateAgentInFirebase(estateAgent: EstateAgent) = this.estateAgentRepository.createEstateAgentInFirestore(estateAgent).await()

    private suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    private suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    private suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    private suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    private suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    private suspend fun createGlobalAddress(address: Address?)
    {
        if (address != null)
        {
            createAddress(address)
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

    //////////////////// UPDATE ////////////////////

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)
}


