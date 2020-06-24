package com.openclassrooms.realestatemanager.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.realestatemanager.models.EstateAgent
import com.openclassrooms.realestatemanager.models.Housing

@Dao
interface EstateAgentDAO
{
    @Query("SELECT * FROM estate_agent")
    fun getAllEstateAgent() : LiveData<List<EstateAgent>>

    @Query("SELECT * FROM estate_agent WHERE last_name =:name")
    fun getEstateAgent(name : String) : LiveData<EstateAgent>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createEstateAgent (estateAgent: EstateAgent) : Long

    @Update
    fun updateEstateAgent (estateAgent: EstateAgent) : Int
}