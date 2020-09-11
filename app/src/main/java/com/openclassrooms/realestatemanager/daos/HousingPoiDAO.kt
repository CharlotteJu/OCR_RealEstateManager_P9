package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.HousingPoi

/**
 * Interface DAO for [HousingPoi] with [RoomDatabase]
 */
@Dao
interface HousingPoiDAO
{
    @Query("SELECT * FROM housing_poi")
    fun getAllHousingPoi() : LiveData<List<HousingPoi>>

    @Query("SELECT * FROM housing_poi WHERE housing_reference =:reference AND poi_type =:type")
    fun getHousingPoi(reference: String, type: String) : LiveData<HousingPoi>

    @Query("SELECT * FROM housing_poi WHERE housing_reference =:reference")
    fun getHousingPoiListFromHousing (reference : String) : LiveData<List<HousingPoi>>

    @Query("SELECT * FROM housing_poi WHERE poi_type =:type")
    fun getHousingPoiListFromPoi (type : String) : LiveData<List<HousingPoi>>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun createHousingPoi(housingPoi: HousingPoi) : Long

    @Update
    suspend fun updateHousingPoi(housingPoi: HousingPoi) : Int

    @Delete
    suspend fun deleteHousingPoi(housingPoi: HousingPoi)
}