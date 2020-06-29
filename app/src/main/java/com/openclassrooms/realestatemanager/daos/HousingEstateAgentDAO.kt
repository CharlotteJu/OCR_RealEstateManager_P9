package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.HousingEstateAgent

@Dao
interface HousingEstateAgentDAO
{
    @Query ("SELECT * FROM housing_estate_agent")
    fun getAllHousingEstateAgent() : LiveData<List<HousingEstateAgent>>

    @Query ("SELECT * FROM housing_estate_agent WHERE housing_reference =:reference AND estate_agent_name =:name")
    fun getHousingEstateAgent(reference: String, name: String) : LiveData<HousingEstateAgent>

    @Query ("SELECT * FROM housing_estate_agent WHERE housing_reference =:reference")
    fun getHousingEstateAgentListFromHousing (reference : String) : LiveData<List<HousingEstateAgent>>

    @Query ("SELECT * FROM housing_estate_agent WHERE estate_agent_name =:name")
    fun getHousingEstateAgentListFromAgent (name : String) : LiveData<List<HousingEstateAgent>>

    @Insert (onConflict =  OnConflictStrategy.REPLACE)
    suspend fun createHousingEstateAgent(housingEstateAgent: HousingEstateAgent) : Long

    @Update
    suspend fun updateHousingEstateAgent(housingEstateAgent: HousingEstateAgent) : Int


}