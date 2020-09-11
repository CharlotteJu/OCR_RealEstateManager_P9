package com.openclassrooms.realestatemanager.models

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Data class to have an [Housing] with it [Address], [Photo], [HousingEstateAgent] (list of [EstateAgent]) and [HousingPoi] (list of [Poi])
 */
data class CompleteHousing (@Embedded var housing: Housing,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var photoList: List<Photo>? = null,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var address: Address? = null,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var estateAgentList: List<HousingEstateAgent>? = null,
                            @Relation(parentColumn = "reference", entityColumn = "housing_reference") var poiList : List<HousingPoi>? = null)
{

    constructor() : this(Housing())

    override fun equals(other: Any?): Boolean {
        return (other is CompleteHousing
                && other.housing == this.housing)
    }

    override fun hashCode(): Int {
        var result = housing.hashCode()
        result = 31 * result + (photoList?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (estateAgentList?.hashCode() ?: 0)
        result = 31 * result + (poiList?.hashCode() ?: 0)
        return result
    }

}