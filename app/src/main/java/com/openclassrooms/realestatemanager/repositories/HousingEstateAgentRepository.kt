package com.openclassrooms.realestatemanager.repositories

import com.openclassrooms.realestatemanager.daos.HousingEstateAgentDAO
import com.openclassrooms.realestatemanager.models.HousingEstateAgent

/**
 * Repository of [HousingEstateAgentDAO]
 */
class HousingEstateAgentRepository (private val housingEstateAgentDAO: HousingEstateAgentDAO)
{
    suspend fun createHousingEstateAgent (housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentDAO.createHousingEstateAgent(housingEstateAgent)

    suspend fun deleteHousingEstateAgent (housingEstateAgent: HousingEstateAgent) = this.housingEstateAgentDAO.deleteHousingEstateAgent(housingEstateAgent)
}