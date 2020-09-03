package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
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
                && other.housing == this.housing
               /* && other.address == this.address
                && other.estateAgentList == this.estateAgentList
                && other.poiList == this.poiList*/)

        //return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = housing.hashCode()
        result = 31 * result + (photoList?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (estateAgentList?.hashCode() ?: 0)
        result = 31 * result + (poiList?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromContentValues(contentValues: ContentValues) : CompleteHousing
        {
            val housing = Housing()
            if (contentValues.containsKey("reference")) housing.ref = contentValues.getAsString("reference")
            if (contentValues.containsKey("type")) housing.type = contentValues.getAsString("type")
            if (contentValues.containsKey("price")) housing.price = contentValues.getAsDouble("price")
            if (contentValues.containsKey("area")) housing.area = contentValues.getAsDouble("area")
            if (contentValues.containsKey("rooms")) housing.rooms = contentValues.getAsInteger("rooms")
            if (contentValues.containsKey("bedrooms")) housing.bedrooms = contentValues.getAsInteger("bedrooms")
            if (contentValues.containsKey("bathrooms")) housing.bathrooms = contentValues.getAsInteger("bathrooms")
            if (contentValues.containsKey("state")) housing.state = contentValues.getAsString("state")
            if (contentValues.containsKey("dateEntry")) housing.dateEntry = contentValues.getAsLong("dateEntry")
            if (contentValues.containsKey("dateSale")) housing.dateSale = contentValues.getAsLong("dateSale")
            if (contentValues.containsKey("description")) housing.description = contentValues.getAsString("description")

            val address = Address()
            if (contentValues.containsKey("id")) address.id = contentValues.getAsString("id")
            if (contentValues.containsKey("street")) address.street = contentValues.getAsString("street")
            if (contentValues.containsKey("zip_code")) address.zipCode = contentValues.getAsInteger("zip_code")
            if (contentValues.containsKey("city")) address.city = contentValues.getAsString("city")
            if (contentValues.containsKey("country")) address.country = contentValues.getAsString("country")
            if (contentValues.containsKey("housing_reference")) address.housingReference = contentValues.getAsString("housing_reference")

            val poi = HousingPoi()
            if (contentValues.containsKey("poi_type")) poi.poiType = contentValues.getAsString("poi_type")
            if (contentValues.containsKey("housing_reference")) poi.housingReference = contentValues.getAsString("housing_reference")

            val photo = Photo()
            if (contentValues.containsKey("uri")) photo.uri = contentValues.getAsString("uri")
            if (contentValues.containsKey("description")) photo.description = contentValues.getAsString("description")
            if (contentValues.containsKey("housing_reference")) photo.description = contentValues.getAsString("housing_reference")

            val estateAgent = HousingEstateAgent()
            if (contentValues.containsKey("housing_reference")) estateAgent.housingReference = contentValues.getAsString("housing_reference")
            if (contentValues.containsKey("estate_agent_name")) estateAgent.estateAgentName = contentValues.getAsString("estate_agent_name")

            return CompleteHousing(housing, listOf(photo), address, listOf(estateAgent), listOf(poi))

        }
    }
}