package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.HousingPoi

@Dao
interface HousingPoiDAO
{
    @Query("SELECT * FROM housing_poi")
    fun getAllHousingPoi() : LiveData<List<HousingPoi>>

    @Query("SELECT * FROM housing_poi WHERE housing_reference =:reference AND poi_type =:type")
    fun getHousingPoi(reference: String, type: String) : LiveData<HousingPoi>

    @Query("SELECT * FROM housing_poi WHERE housing_reference =:reference")
    fun getHousingPoiFromHousing (reference : String) : LiveData<HousingPoi>

    @Query("SELECT * FROM housing_poi WHERE poi_type =:type")
    fun getHousingPoiFromPoi (type : String) : LiveData<HousingPoi>

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun createHousingPoi(housingPoi: HousingPoi) : Long

    @Update
    fun updateHousingPoi(housingPoi: HousingPoi) : Int
}