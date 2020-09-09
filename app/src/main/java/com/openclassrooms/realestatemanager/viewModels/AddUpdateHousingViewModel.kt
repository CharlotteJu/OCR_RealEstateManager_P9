package com.openclassrooms.realestatemanager.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.notifications.NotificationWorker
import com.openclassrooms.realestatemanager.repositories.*
import com.openclassrooms.realestatemanager.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * View Model to add and update [Housing], [Address], [Photo], [HousingEstateAgent], [HousingPoi] --> AddHousingFragment & EditHousingFragment
 */
class AddUpdateHousingViewModel(private val housingRepository: HousingRepository,
                                private val addressRepository: AddressRepository,
                                private val photoRepository: PhotoRepository,
                                private val housingEstateAgentRepository: HousingEstateAgentRepository,
                                private val housingPoiRepository: HousingPoiRepository,
                                private val estateAgentRepository: EstateAgentRepository,
                                private val placesPoiRepository : PlacesPoiRepository)
                                : ViewModel()
{

    //////////////////// GET ////////////////////

    fun getEstateAgentList() : LiveData<List<EstateAgent>> = this.estateAgentRepository.getAllEstateAgent()

    fun getCompleteHousing(reference: String) : LiveData<CompleteHousing> = this.housingRepository.getCompleteHousing(reference)

    //////////////////// CREATE ////////////////////

    /**
     * Final method to create a CompleteHousing in RoomDatabase
     */
    fun createGlobalHousing (housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?, context: Context, key : String, isInternetAvailable : Boolean )
    {
        viewModelScope.launch  (Dispatchers.IO)
        {

            val thread = viewModelScope.async {
                createHousing(housing)
                createGlobalAddress(address, context, housing.ref, isInternetAvailable, key)
                createAllPhoto(photoList)
                createAllHousingEstateAgent(estateAgentList)
            }
            thread.await()

            // Launch function on the MainThread !!
            withContext(Dispatchers.Main) {
                val sharedPreferencesNotification = context.getSharedPreferences(NOTIFICATION_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                val isNotification = sharedPreferencesNotification.getBoolean(NOTIFICATION_TAG, true)
                if (isNotification) NotificationWorker.showNotification(context)}
        }

    }

    private suspend fun createHousing(housing : Housing) = this.housingRepository.createHousing(housing)

    private suspend fun createAddress(address: Address) = this.addressRepository.createAddress(address)

    private suspend fun createPhoto(photo: Photo) = this.photoRepository.createPhoto(photo)

    private suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.createHousingEstateAgent(housingEstateAgent)

    private suspend fun createHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.createHousingPoi(housingPoi)

    private suspend fun getPoiFromPlaces(location : String, radius : Int, key : String) = this.placesPoiRepository.getPoiFromPlaces(location, radius, key)

    /**
     * Create HousingPoi according with the Address
     */
    private suspend fun configurePoi(ref : String, location: String, context: Context, key : String)
    {
        val listTypePoi = UtilsKotlin.getListTypePoi(context)
        val listPoiPlaces = getPoiFromPlaces(location, 500, key).results

        for (type in listTypePoi)
        {
            var isPresent = false
            for (place in listPoiPlaces)
            {
                if (place.types.contains(type))
                {
                    val housingPoi = HousingPoi(ref, type)
                    createHousingPoi(housingPoi)
                    isPresent = true
                }
                if (isPresent) break
            }
        }
    }

    private suspend fun createGlobalAddress(address: Address?, context: Context, ref: String, isInternetAvailable: Boolean, key: String)
    {
        if (address != null)
        {
            createAddress(address)

            val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)
            if (location != null && location != ERROR_GEOCODER_ADDRESS)
            {
                if (isInternetAvailable) configurePoi(ref, location, context, key)
            }
        }
    }

    private suspend fun createAllHousingEstateAgent(estateAgentList: List<HousingEstateAgent>?)
    {
        if (estateAgentList != null)
        {
            for (estate in estateAgentList)
            {
                createHousingEstateAgent(estate)
            }
        }
    }

    private suspend fun createAllPhoto(photoList: List<Photo>?)
    {
        if (photoList != null)
        {
            for (photo in photoList)
            {
                createPhoto(photo)
            }
        }
    }


    //////////////////// UPDATE ////////////////////

    /**
     * Final method to update a CompleteHousing in RoomDatabase
     */
    fun updateGlobalHousing (completeHousing: CompleteHousing, housing: Housing, address: Address?, photoList: List<Photo>?, estateAgentList: List<HousingEstateAgent>?, context: Context, key : String, isInternetAvailable : Boolean)
    {
        viewModelScope.launch (Dispatchers.IO)
        {

            if (housing.ref == completeHousing.housing.ref)
            {
                housing.lastUpdate = Utils.getTodayDateGood()
                updateHousing(housing)
                updateGlobalAddress(address, completeHousing, context, housing.ref, isInternetAvailable, key)
                updateAllEstateHousingEstateAgent(estateAgentList, completeHousing)
                updateAllPhoto(photoList, completeHousing)
            }
        }
    }

    private suspend fun updateHousing(housing: Housing) = this.housingRepository.updateHousing(housing)

    private suspend fun updateAddress(address: Address) = this.addressRepository.updateAddress(address)

    private suspend fun deleteAddress(address: Address) = this.addressRepository.deleteAddress(address)

    private suspend fun updatePhoto(photo: Photo) = this.photoRepository.updatePhoto(photo)

    private suspend fun deletePhoto(photo: Photo) = this.photoRepository.deletePhoto(photo)

    private suspend fun deleteHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentRepository.deleteHousingEstateAgent(housingEstateAgent)

    private suspend fun deleteHousingPoi(housingPoi: HousingPoi) = this.housingPoiRepository.deleteHousingPoi(housingPoi)

    private suspend fun updateGlobalAddress(address: Address?, completeHousing: CompleteHousing, context: Context, ref : String, isInternetAvailable: Boolean, key: String)
    {
       if (address != null)
        {
            if (completeHousing.address != null && address != completeHousing.address) {

                updateAddress(address)

                val location = UtilsKotlin.getGeocoderAddress(address.toString(), context)
                if (location != null && location != ERROR_GEOCODER_ADDRESS) {

                    if (completeHousing.poiList != null && completeHousing.poiList!!.isNotEmpty())
                    {
                        for (i in completeHousing.poiList!!)
                        {
                            deleteHousingPoi(i)
                        }
                    }
                    if (isInternetAvailable) configurePoi(ref, location, context, key)
                }
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
                if (!completeHousing.photoList!!.contains(i))
                {
                    createPhoto(i)
                }

                else
                {
                    val index = completeHousing.photoList!!.indexOf(i)
                    if (completeHousing.photoList!![index].description != i.description)
                    {
                        updatePhoto(i)
                    }
                }
            }

            for (i in completeHousing.photoList!!)
            {
                if (!photoList.contains(i))
                {
                    deletePhoto(i)
                }
            }
        }
        else if (photoList == null && completeHousing.photoList != null)
        {
            for (photo in completeHousing.photoList!!) //TODO-Q : Pourquoi besoin de l'assert ici et pas en dessous ?
            {
                deletePhoto(photo)
            }
        }
        else if (photoList != null && completeHousing.photoList == null)
        {
            for (photo in photoList)
            {
                createPhoto(photo)
            }
        }
    }

}