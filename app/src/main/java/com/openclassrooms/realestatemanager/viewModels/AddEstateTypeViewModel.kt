package com.openclassrooms.realestatemanager.viewModels

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.models.Poi
import com.openclassrooms.realestatemanager.repositories.EstateAgentRepository
import com.openclassrooms.realestatemanager.repositories.PoiRepository

/**
 * View Model to add [EstateAgent] and types of [Poi] --> AddEstateAgentFragment
 */
class AddEstateTypeViewModel (private val estateAgentRepository: EstateAgentRepository,
                              private val poiRepository: PoiRepository)
                                : ViewModel()
{
    //////////////////// CREATE ////////////////////

    suspend fun createEstateAgent(estateAgent: EstateAgent) = this.estateAgentRepository.createEstateAgent(estateAgent)

    suspend fun createPoi(poi: Poi) = this.poiRepository.createPoi(poi)


    //////////////////// UPDATE ////////////////////


    suspend fun updateEstateAgent(estateAgent: EstateAgent) = this.estateAgentRepository.updateEstateAgent(estateAgent)

    suspend fun updatePoi(poi: Poi) = this.poiRepository.updatePoi(poi)
}