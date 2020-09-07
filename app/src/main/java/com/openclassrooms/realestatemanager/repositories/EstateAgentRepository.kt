package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.api.EstateAgentHelper
import com.openclassrooms.realestatemanager.daos.EstateAgentDAO
import com.openclassrooms.realestatemanager.models.EstateAgent
import kotlinx.coroutines.coroutineScope

/**
 * Repository of [EstateAgentDAO]
 */
class EstateAgentRepository (private val estateAgentDAO: EstateAgentDAO)
{
    fun getAllEstateAgent() : LiveData<List<EstateAgent>> = this.estateAgentDAO.getAllEstateAgent()

    fun getEstateAgent(name : String) : LiveData<EstateAgent> = this.estateAgentDAO.getEstateAgent(name)

    suspend fun createEstateAgent(estateAgent: EstateAgent) = this.estateAgentDAO.createEstateAgent(estateAgent)

    suspend fun updateEstateAgent(estateAgent: EstateAgent) = this.estateAgentDAO.updateEstateAgent(estateAgent)

    suspend fun deleteEstateAgent(estateAgent: EstateAgent) = this.estateAgentDAO.deleteEstateAgent(estateAgent)

    //////////////// FIRESTORE ////////////////

    suspend fun getEstateAgentListFromFirestore() = EstateAgentHelper.getEstateAgentListFromFirestore()

    suspend fun createEstateAgentInFirestore(estateAgent: EstateAgent) = coroutineScope {
        EstateAgentHelper.createEstateAgentListInFirestore(estateAgent)
    }
}