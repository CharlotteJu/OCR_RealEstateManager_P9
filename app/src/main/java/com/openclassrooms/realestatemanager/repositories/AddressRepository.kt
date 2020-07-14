package com.openclassrooms.realestatemanager.repositories


import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.daos.AddressDAO
import com.openclassrooms.realestatemanager.models.Address

class AddressRepository (private val addressDAO: AddressDAO)
{
    fun getAllAddress() : LiveData<List<Address>> = this.addressDAO.getAllAddress()

    fun getAddressFromHousing(reference : String) : LiveData<Address> = this.addressDAO.getAddressFromHousing(reference)

    fun getAddressFromId(id : String) : LiveData<Address> = this.addressDAO.getAddressFromId(id)

    suspend fun createAddress(address: Address) = this.addressDAO.createAddress(address)

    suspend fun updateAddress(address: Address) = this.addressDAO.updateAddress(address)

    suspend fun deleteAddress (address: Address) = this.addressDAO.deleteAddress(address)
}