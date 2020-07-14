package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.daos.HousingEstateAgentDAO
import com.openclassrooms.realestatemanager.models.HousingEstateAgent

class HousingEstateAgentRepository (private val housingEstateAgentDAO: HousingEstateAgentDAO)
{
    fun getAllHousingEstateAgent() : LiveData<List<HousingEstateAgent>> = this.housingEstateAgentDAO.getAllHousingEstateAgent()

    fun getHousingEstateAgent(reference : String, name : String) : LiveData<HousingEstateAgent> = this.housingEstateAgentDAO.getHousingEstateAgent(reference, name)

    fun getHousingEstateAgentFromHousing(reference: String) : LiveData<List<HousingEstateAgent>> = this.housingEstateAgentDAO.getHousingEstateAgentListFromHousing(reference)

    fun getHousingEstateAgentFromAgent(name: String) : LiveData<List<HousingEstateAgent>> = this.housingEstateAgentDAO.getHousingEstateAgentListFromAgent(name)

    suspend fun createHousingEstateAgent (housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentDAO.createHousingEstateAgent(housingEstateAgent)

    suspend fun updateHousingEstateAgent(housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentDAO.updateHousingEstateAgent(housingEstateAgent)

    suspend fun deleteHousingEstateAgent (housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentDAO.deleteHousingEstateAgent(housingEstateAgent)

}