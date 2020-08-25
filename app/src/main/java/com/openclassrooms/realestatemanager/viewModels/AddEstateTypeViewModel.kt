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
 */
class AddEstateTypeViewModel (private val estateAgentRepository: EstateAgentRepository,
                              private val poiRepository: PoiRepository)
                                : ViewModel()
{

    //////////////////// GET ////////////////////

    fun getAllEstateAgent() = this.estateAgentRepository.getAllEstateAgent()

    fun getEstateAgent(lastName : String) = this.estateAgentRepository.getEstateAgent(lastName)

    //////////////////// CREATE ////////////////////

    private suspend fun createEstateAgent(estateAgent: EstateAgent) = this.estateAgentRepository.createEstateAgent(estateAgent)

    private suspend fun createPoi(poi: Poi) = this.poiRepository.createPoi(poi)

    fun createGlobalEstateAgent(estateAgent: EstateAgent)
    {
        viewModelScope.launch {
            createEstateAgent(estateAgent)
        }
    }


    //////////////////// UPDATE ////////////////////


    private suspend fun updateEstateAgent (estateAgent: EstateAgent) = this.estateAgentRepository.updateEstateAgent(estateAgent)

    private suspend fun updatePoi(poi: Poi) = this.poiRepository.updatePoi(poi)

    fun updateGlobalEstateAgent(estateAgent: EstateAgent)
    {
        viewModelScope.launch {
            updateEstateAgent(estateAgent)
        }
    }
}