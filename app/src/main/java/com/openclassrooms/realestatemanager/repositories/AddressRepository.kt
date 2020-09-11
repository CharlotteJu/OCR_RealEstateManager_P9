package com.openclassrooms.realestatemanager.repositories


import com.openclassrooms.realestatemanager.daos.AddressDAO
import com.openclassrooms.realestatemanager.models.Address

/**
 * Repository of [AddressDAO]
 */
class AddressRepository (private val addressDAO: AddressDAO)
{
    suspend fun createAddress(address: Address) = this.addressDAO.createAddress(address)

    suspend fun updateAddress(address: Address) = this.addressDAO.updateAddress(address)

    suspend fun deleteAddress (address: Address) = this.addressDAO.deleteAddress(address)
}