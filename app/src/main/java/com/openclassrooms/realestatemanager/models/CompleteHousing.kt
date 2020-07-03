package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteHousing (@Embedded var housing: Housing,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var photoList: List<Photo>? = null,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var address: Address? = null,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var estateAgentList: List<HousingEstateAgent>? = null,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var poiList : List<HousingPoi>? = null)
{
}