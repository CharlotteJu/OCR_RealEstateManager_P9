package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.openclassrooms.realestatemanager.api.CompleteHousingHelper
import com.openclassrooms.realestatemanager.daos.AddressDAO
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing

/**
 * Repository of [HousingDAO], and getting [CompleteHousing]
 */
class HousingRepository(private val housingDao : HousingDAO)
{

    fun getAllHousing() : LiveData<List<Housing>> = this.housingDao.getAllHousing()

    fun getHousing(reference : String) : LiveData<Housing> = this.housingDao.getHousing(reference)

    suspend fun createHousing(housing: Housing) = this.housingDao.createHousing(housing)

    suspend fun updateHousing (housing: Housing) = this.housingDao.updateHousing(housing)

    /**
     * Just for test
     */
    suspend fun deleteHousing (housing: Housing) = this.housingDao.deleteHousing(housing)

    fun getCompleteHousing(reference: String) : LiveData<CompleteHousing> = this.housingDao.getCompleteHousing(reference)

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingDao.getAllCompleteHousing()


    //////////////// FIRESTORE ////////////////

    fun getListCompleteHousingFromFirestore() : Query
    {
        return CompleteHousingHelper.getListCompleteHousingFromFirestore()
    }

    fun getCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<DocumentSnapshot>
    {
        return CompleteHousingHelper.getCompleteHousingFromFirestore(completeHousing)
    }

    fun createCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<Void>
    {
        return CompleteHousingHelper.createCompleteHousingFromFirestore(completeHousing)
    }

    fun deleteCompleteHousingFromFirestore(completeHousing: CompleteHousing) : Task<Void>
    {
        return CompleteHousingHelper.deleteCompleteHousingFromFirestore(completeHousing)
    }

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