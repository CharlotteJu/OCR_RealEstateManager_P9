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
    fun getAddressFromHousing(reference : String) : LiveData<Address>

    @Query("SELECT * FROM address WHERE id =:id")
    fun getAddressFromId(id : String) : LiveData<Address>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAddress (address: Address) : Long

    @Update
    suspend fun updateAddress (address: Address) : Int

    @Delete
    suspend fun deleteAddress (address: Address)
}