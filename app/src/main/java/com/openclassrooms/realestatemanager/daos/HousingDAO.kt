package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Housing

@Dao
interface HousingDAO
{
    @Query("SELECT * FROM housing")
    fun getAllHousing() : LiveData<List<Housing>>

    @Query ("SELECT * FROM housing WHERE reference =:reference")
    fun getHousing(reference : String) : LiveData<Housing>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun createHousing (housing: Housing) : Long

    @Update
    suspend fun updateHousing (housing: Housing) : Int
}