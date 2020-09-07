package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * View Model to get [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> ListFragment & MapFragment
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

    fun syncEstateAgentWithFirebase(listRoom: List<EstateAgent>)
    {
        viewModelScope.launch {
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


                for (eFirestore in listFirestore) {
                    if (!listRoom.contains(eFirestore)) {
                        createEstateAgentInRoom(eFirestore)
                    } else {
                        val index = listRoom.indexOf(eFirestore)
                        if (listRoom[index].lastUpdateEstate > eFirestore.lastUpdateEstate) {
                            createEstateAgentInFirebase(listRoom[index])
                        } else if (listRoom[index].lastUpdateEstate < eFirestore.lastUpdateEstate) {
                            createEstateAgentInRoom(eFirestore)
                        }
                    }
                }

            }
        }
    }

    fun syncCompleteHousingWithFirebase(listRoom : List<CompleteHousing>, context: Context) {
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


                for (hFirestore in listFirestore) {
                    if (!listRoom.contains(hFirestore)) {
                        createCompleteHousingOnRoom(hFirestore)
                    } /*else {
                        val index = listRoom.indexOf(hFirestore)
                        if (listRoom[index].housing.lastUpdate > hFirestore.housing.lastUpdate) {
                            createCompleteHousingOnFirestore(listRoom[index])
                        } else if (listRoom[index].housing.lastUpdate < hFirestore.housing.lastUpdate) {
                            createCompleteHousingOnRoom(hFirestore)
                        }
                    }*/
                }

            }
        }
    }


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

    private suspend fun updatePhotoFirebase(photo: Photo) = this.photoRepository.updatePhotoFirebase(photo)

    private suspend fun uploadAPhotoInFirestore(photo : Photo)
    {
        if (photo.url_firebase == null)
        {
            val test = housingRepository.pushPhotoOnFirebaseStorage(photo)
            if (test != null)
            {
                val test2 = test.storage.downloadUrl.await()
                photo.url_firebase = test2.toString()
                updatePhoto(photo)
            }

            /*viewModelScope.launch {
                val test = housingRepository.testStoragePhoto(photo)
                if (test != null)
                {
                    val test2 = test.storage.downloadUrl.await()
                    photo.url_firebase = test2.toString()
                    updatePhoto(photo)
                    val debugPhoto = photoRepository.getPhotoFromUri(photo.uri)
                    /*val thread = async { updatePhoto(photo)  }
                    thread.await()*/
                }
            }*/

            /*val ref = FirebaseStorage.getInstance().getReference(FIREBASE_STORAGE_REF)
            ref.child(photo.uri).putFile(photo.uri.toUri()).addOnSuccessListener { taskSnapshot -> //TODO : Il faut que l'accès aux images soit dispo
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    photo.url_firebase = it.toString()
                    viewModelScope.launch {
                        val thread = async { updatePhoto(photo) }
                        thread.await()
                         }

                }
            }*/
        }
    }


    //////////////////// CREATE ////////////////////

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

    //////////////////// UPDATE ////////////////////

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    /*private suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    private suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    private suspend fun updateGlobalAddress(address: Address?, completeHousing: CompleteHousing, context: Context)
    {
        if (address != null)
        {
            if (completeHousing.address != null && address != completeHousing.address)
            {
                val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)
                if (location != null && location != ERROR_GEOCODER_ADDRESS)
                    updateAddress(address)
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


    private fun updateGlobalHousing (housingFromRoom : CompleteHousing, housingFromFirestore : CompleteHousing, context: Context)
    {
        viewModelScope.launch (Dispatchers.IO)
        {

            val thread = viewModelScope.async {
                if (housingFromRoom != housingFromFirestore) {
                    if (housingFromRoom.housing != housingFromFirestore.housing) updateHousing(housingFromRoom.housing)
                    if (housingFromRoom.address != housingFromFirestore.address) {
                        updateGlobalAddress(housingFromRoom.address, housingFromFirestore, context)
                    }
                    if (housingFromRoom.photoList != housingFromFirestore.photoList) {
                        updateAllPhoto(housingFromRoom.photoList, housingFromFirestore)
                    }
                    if (housingFromRoom.estateAgentList != housingFromFirestore.estateAgentList) {
                        updateAllEstateHousingEstateAgent(housingFromRoom.estateAgentList, housingFromFirestore)
                    }
                    if (housingFromRoom.poiList != housingFromFirestore.poiList) {
                        updateAllHousingPoi(housingFromRoom.poiList, housingFromFirestore)
                    }
                }
            }


        }
    }*/

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


}


