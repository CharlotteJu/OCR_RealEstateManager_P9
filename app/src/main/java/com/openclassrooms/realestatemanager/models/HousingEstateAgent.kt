package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.openclassrooms.realestatemanager.utils.STRING_EMPTY

@Entity (tableName = "housing_estate_agent",
        primaryKeys = ["housing_reference", "estate_agent_name"],
        foreignKeys = [ForeignKey(onDelete = CASCADE, entity = Housing::class,
                                    parentColumns = ["reference"],
                                    childColumns = ["housing_reference"]),
                        ForeignKey(onDelete = CASCADE, entity = EstateAgent::class,
                                    parentColumns = ["last_name"],
                                    childColumns = ["estate_agent_name"])])

data class HousingEstateAgent constructor(@ColumnInfo (name = "housing_reference", index = true) var housingReference : String= STRING_EMPTY,
                                          @ColumnInfo (name = "estate_agent_name", index = true) var estateAgentName : String= STRING_EMPTY)

{
    companion object {
        fun fromContentValues(contentValues: ContentValues) : HousingEstateAgent
        {
            val estateAgent = HousingEstateAgent()
            if (contentValues.containsKey("housing_reference")) estateAgent.housingReference = contentValues.getAsString("housing_reference")
            if (contentValues.containsKey("estate_agent_name")) estateAgent.estateAgentName = contentValues.getAsString("estate_agent_name")

            return estateAgent
        }
    }

}