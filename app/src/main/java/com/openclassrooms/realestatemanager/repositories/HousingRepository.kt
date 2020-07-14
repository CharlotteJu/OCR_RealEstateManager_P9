package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.daos.HousingDAO
import com.openclassrooms.realestatemanager.models.CompleteHousing
import com.openclassrooms.realestatemanager.models.Housing

class HousingRepository(private val housingDao : HousingDAO)
{

    fun getAllHousing() : LiveData<List<Housing>> = this.housingDao.getAllHousing()

    fun getHousing(reference : String) : LiveData<Housing> = this.housingDao.getHousing(reference)

    suspend fun createHousing(housing: Housing) = this.housingDao.createHousing(housing)

    suspend fun updateHousing (housing: Housing) = this.housingDao.updateHousing(housing)

    suspend fun deleteHousing (housing: Housing) = this.housingDao.deleteHousing(housing)

    fun getCompleteHousing(reference: String) : LiveData<CompleteHousing> = this.housingDao.getCompleteHousing(reference)

    fun getAllCompleteHousing() : LiveData<List<CompleteHousing>> = this.housingDao.getAllCompleteHousing()

}