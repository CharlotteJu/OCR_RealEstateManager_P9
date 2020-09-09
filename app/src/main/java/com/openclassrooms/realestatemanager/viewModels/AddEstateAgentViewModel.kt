package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.models.Poi
import com.openclassrooms.realestatemanager.repositories.EstateAgentRepository
import com.openclassrooms.realestatemanager.repositories.PoiRepository
import kotlinx.coroutines.launch

/**
 * View Model to add [EstateAgent] and types of [Poi] --> AddEstateAgentFragment
 * @param poiRepository not used for the moment, but it can be useful in the future
 */
class AddEstateAgentViewModel (private val estateAgentRepository: EstateAgentRepository,
                               private val poiRepository: PoiRepository)
                                : ViewModel()
{

    //////////////////// GET ////////////////////

    fun getAllEstateAgent() = this.estateAgentRepository.getAllEstateAgent()

    //////////////////// CREATE ////////////////////

    private suspend fun createEstateAgent(estateAgent: EstateAgent) = this.estateAgentRepository.createEstateAgent(estateAgent)

    fun createGlobalEstateAgent(estateAgent: EstateAgent)
    {
        viewModelScope.launch {
            createEstateAgent(estateAgent)
        }
    }

    //////////////////// UPDATE ////////////////////

    private suspend fun updateEstateAgent (estateAgent: EstateAgent) = this.estateAgentRepository.updateEstateAgent(estateAgent)

    fun updateGlobalEstateAgent(estateAgent: EstateAgent)
    {
        viewModelScope.launch {
            updateEstateAgent(estateAgent)
        }
    }
}