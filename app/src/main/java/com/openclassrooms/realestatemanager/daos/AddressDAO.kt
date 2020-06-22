package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.Address
import com.openclassrooms.realestatemanager.models.Housing

@Dao
interface AddressDAO
{
    @Query("SELECT * FROM address")
    fun getAllAddress() : LiveData<List<Address>>

    @Query("SELECT * FROM address WHERE housing_reference =:reference")
    fun getAddress(reference : String) : LiveData<Address>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createAddress (address: Address) : Long

    @Update
    fun updateAddress (address: Address) : Int
}