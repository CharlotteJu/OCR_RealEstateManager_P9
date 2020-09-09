package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.api.CompleteHousingHelper
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing
import com.openclassrooms.realestatemanager.models.Photo
import kotlinx.coroutines.coroutineScope

/**
 * Repository of [HousingDAO], and getting [CompleteHousing]
 */
class HousingRepository(private val housingDao : HousingDAO)
{

    suspend fun createHousing(housing: Housing) = this.housingDao.createHousing(housing)

    suspend fun updateHousing (housing: Housing) = this.housingDao.updateHousing(housing)

    /**
     * Just for test
     */
    suspend fun deleteHousing (housing: Housing) = this.housingDao.deleteHousing(housing)

    fun getCompleteHousing(reference: String) : LiveData<CompleteHousing> = this.housingDao.getCompleteHousing(reference)

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingDao.getAllCompleteHousing()


    //////////////// FIRESTORE ////////////////

    suspend fun  createCompleteHousingInFirestore(completeHousing: CompleteHousing) = coroutineScope {
        CompleteHousingHelper.createCompleteHousingInFirestore(completeHousing)
    }

    suspend fun getCompleteHousingListFromFirestore() = CompleteHousingHelper.getCompleteHousingListFromFirestore()

    suspend fun pushPhotoOnFirebaseStorage(photo: Photo) = CompleteHousingHelper.pushPhotoOnFirebaseStorage(photo)


    //////////////// FILTER ////////////////

    fun getListFilter(type : String? = null,
                      priceLower : Double? = null,
                      priceHigher : Double? = null,
                      areaLower : Double? = null,
                      areaHigher : Double? = null,
                      roomLower : Int? = null,
                      roomHigher : Int? = null,
                      bedRoomLower : Int? = null,
                      bedRoomHigher : Int? = null,
                      bathRoomLower : Int? = null,
                      bathRoomHigher : Int? = null,
                      state : String? = null,
                      dateEntry : Long? = null,
                      dateSale : Long? = null,
                      city : String? = null,
                      country : String? = null,
                      typePoi : String? = null,
                      numberPhotos : Int? = null,
                      estateAgent : String? = null): LiveData<List<CompleteHousing>>
    {
       return this.housingDao.getListCompleteHousingFilter(type,
               priceLower, priceHigher, areaLower, areaHigher,
               roomLower, roomHigher, bedRoomLower, bedRoomHigher,
               bathRoomLower, bathRoomHigher, state, dateEntry,
               dateSale, city, country, typePoi, estateAgent, numberPhotos)
    }

}